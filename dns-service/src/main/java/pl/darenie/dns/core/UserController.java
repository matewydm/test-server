package pl.darenie.dns.core;

import com.google.firebase.auth.UserRecord;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import pl.darenie.dns.core.firebase.notification.FcmNotificationService;
import pl.darenie.dns.core.mapper.CyclicAccountingMapper;
import pl.darenie.dns.core.mapper.FriendMapper;
import pl.darenie.dns.jpa.*;
import pl.darenie.dns.core.firebase.authorization.FirebaseUserService;
import pl.darenie.dns.core.mapper.UserMapper;
import pl.darenie.dns.dao.FriendRepository;
import pl.darenie.dns.dao.InvitationRepository;
import pl.darenie.dns.dao.UserRepository;
import pl.darenie.dns.model.dto.*;
import pl.darenie.dns.model.enums.*;
import pl.darenie.dns.model.exception.CoreException;
import pl.darenie.dns.model.rest.request.DeviceRefreshRequest;
import pl.darenie.dns.model.rest.request.DeviceTokenRequest;
import pl.darenie.dns.model.rest.request.FriendsInvitationRequest;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@PropertySource("classpath:notification.properties")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.toString());

    @Autowired
    private FirebaseUserService firebaseUserService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InvitationRepository invitationRepository;
    @Autowired
    private FriendRepository friendRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private CyclicAccountingMapper cyclicAccountingMapper;
    @Autowired
    private UserDeviceService userDeviceService;
    @Autowired
    private FcmNotificationService notificationService;

    @Value("${title.friend.update}")
    public String TITLE_FRIEND_UPDATE;
    @Value("${body.friend.accounting}")
    public String BODY_FRIEND_ACCOUNTING;
    @Value("${title.friend.invitation}")
    public String TITLE_FRIEND_INVITATION;
    @Value("${body.friend.invitation}")
    public String BODY_FRIEND_INVITATION;
    @Value("${title.charge.remind}")
    public String TITLE_CHARGE_REMIND;
    @Value("${body.charge.remind}")
    public String BODY_CHARGE_REMIND;

    public void registerUser(UserDTO userDTO) throws CoreException {
        UserRecord userRecord = firebaseUserService.registerFirebaseUser(userDTO);
        userDTO.setFirebaseToken(userRecord.getUid());
        User user = userMapper.mapToJpa(userDTO);
        userRepository.save(user);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(UserDTO userDTO) {
        if (userDTO.getFirebaseToken() != null) {
            User user = userRepository.getOne(userDTO.getFirebaseToken());
            user = userMapper.copyValues(user, userDTO);
            userRepository.save(user);
        }
    }

    public UserDTO getOneDto(String userToken) {
        return userMapper.mapToDto(userRepository.getOne(userToken));
    }

    public User getOne(String userToken) {
        return userRepository.getOne(userToken);
    }

    public void updateDevice(String userToken, DeviceTokenRequest tokenRequest) {
        User user = userRepository.findOne(userToken);
        userDeviceService.pairUserAndDevice(user, tokenRequest.getDeviceToken());
    }

    public void deleteDevice(DeviceTokenRequest tokenRequest) {
        userDeviceService.separateDeviceFromUser(tokenRequest.getDeviceToken());
    }

    public void refreshDevice(DeviceRefreshRequest deviceRefreshRequest) {
        userDeviceService.checkTokenRefresh(deviceRefreshRequest.getOldDeviceToken(),deviceRefreshRequest.getNewDeviceToken());
    }

    public List<UserDTO> getFriendsByToken(String userToken) {
        Set<Friend> friendSet = userRepository.findOne(userToken).getFriends();
        return friendSet.stream()
                .map(Friend::getFriend)
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getChecksByToken(String userToken) {
        Set<Friend> friendSet = userRepository.findOne(userToken).getFriends();
        List<User> checks =  friendSet.stream()
                .map(Friend::getFriend)
                .collect(Collectors.toList());
        checks.add(userRepository.findOne(userToken));
        return checks.stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getForeignersByToken(String userToken) {
        List<User> foreigners = userRepository.findAll();
        foreigners.removeAll(
                userRepository.findOne(userToken).getFriends().stream()
                        .map(Friend::getFriend)
                        .collect(Collectors.toList())
        );
        foreigners.removeAll(
                userRepository.findOne(userToken).getSentInvitations().stream()
                        .map(Invitation::getReceiver)
                        .collect(Collectors.toList())
        );
        foreigners.removeAll(
                userRepository.findOne(userToken).getReceivedInvitations().stream()
                        .map(Invitation::getSender)
                        .collect(Collectors.toList())
        );
        foreigners.remove(userRepository.findOne(userToken));
        return foreigners.stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public FriendDTO getFriendDetails(String userToken, String friendToken) {
        Friend friend = friendRepository.findByUserIdAndFriendId(userToken, friendToken);
        Friend inverseFriend = friendRepository.findByUserIdAndFriendId(friendToken, userToken);
        FriendDTO friendDTO = friendMapper.mapToDto(friend);
        friendDTO.setBalance(new BalanceDTO(friend.getDebt(), inverseFriend.getDebt()));
        return friendDTO;
    }

    public void inviteFriends(String userToken, FriendsInvitationRequest friendsInvitationRequest) throws CoreException {
        User user = userRepository.findOne(userToken);
        for (String receiverToken : friendsInvitationRequest.getUsers()) {
            User receiver = userRepository.findOne(receiverToken);
            Invitation invitation = invitationRepository.findAllBySenderTokenAndReceiverTokenAnd(userToken, receiverToken);
            if (invitation != null) {
                if (invitation.getStatus().equals(InvitationStatus.ACCEPTED)) {
                    throw new CoreException("User is already a friend", ErrorCode.USER_ALREADY_FRIEND);
                } else if (invitation.getStatus().equals(InvitationStatus.REJECTED)) {
                    invitation.setStatus(InvitationStatus.AWAITING);
                }
            } else {
                invitation = new Invitation();
                invitation.setSender(user);
                invitation.setReceiver(receiver);
                invitation.setStatus(InvitationStatus.AWAITING);
            }
            invitationRepository.save(invitation);
            notificationService.sendUserNotification(receiver, TITLE_FRIEND_INVITATION, BODY_FRIEND_INVITATION.replaceAll("\\$name\\$", user.getDisplayName()));
        }

    }

    public void answerInvitation(String receiverToken, String senderToken, InvitationStatus status) throws CoreException {
        Invitation invitation = invitationRepository.findAllBySenderTokenAndReceiverTokenAnd(senderToken,receiverToken);
        invitationRepository.delete(invitation);
        if (status.equals(InvitationStatus.ACCEPTED)) {
            Friend friendOne = new Friend(userRepository.findOne(senderToken),userRepository.findOne(receiverToken));
            Friend friendTwo = new Friend(userRepository.findOne(receiverToken),userRepository.findOne(senderToken));
            friendRepository.save(friendOne);
            friendRepository.save(friendTwo);
        }
    }

    public List<UserDTO> getInvitationsByReceiverToken(String firebaseToken) {
        Set<Invitation> friendSet = userRepository.findOne(firebaseToken).getReceivedInvitations();
        return friendSet.stream()
                .map(Invitation::getSender)
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<UserDTO> getAllDTO() {
        return userRepository.findAll().stream()
                .map(userMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void updateFriend(String userToken, FriendDTO friendDTO) {
        CyclicAccountingDTO accounting = friendDTO.getCyclicAccounting();
        Friend friendOne = friendRepository.findByUserIdAndFriendId(userToken, friendDTO.getFirebaseToken());
        Friend friendTwo = friendRepository.findByUserIdAndFriendId(friendDTO.getFirebaseToken(),userToken);
        friendOne.setCyclicAccounting(cyclicAccountingMapper.remapJpa(friendOne.getCyclicAccounting(),accounting));
        friendTwo.setCyclicAccounting(cyclicAccountingMapper.remapJpa(friendTwo.getCyclicAccounting(),accounting));

        notificationService.sendUserNotification(friendTwo.getUser(), TITLE_FRIEND_UPDATE, BODY_FRIEND_ACCOUNTING.replaceAll("\\$name\\$", friendOne.getUser().getDisplayName()));
        friendRepository.save(friendOne);
        friendRepository.save(friendTwo);
    }

    public CyclicAccounting getAccounting(String tokenOne, String tokenTwo) {
        Friend friend = friendRepository.findByUserIdAndFriendId(tokenOne, tokenTwo);
        if (friend == null) {
            return null;
        }
        return friend.getCyclicAccounting();
    }

    public void notifyChargers() {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        List<BillPriority> priorityList = new ArrayList<>();
        switch (day%3) {
            case 0 : {
                priorityList.addAll(Arrays.asList(BillPriority.values()));
            }
            case 1 : {
                priorityList.add(BillPriority.MEDIUM);
                priorityList.add(BillPriority.HIGH);
            }
            case 2 : {
                priorityList.add(BillPriority.HIGH);
            }
            default: break;
        }
        List<User> chargers = userRepository.findChargerByBillPriorityAndStatusAndVisibility(priorityList, SettlementStatus.UNPAID, Visibility.VISIBLE);
        chargers.forEach(charger -> notificationService.sendUserNotification(charger,TITLE_CHARGE_REMIND, BODY_CHARGE_REMIND));
    }

    public void updateCache(String payerToken, String chargerToken, Double charge) {
        Friend friendEntity = friendRepository.findByUserIdAndFriendId(payerToken,chargerToken);
        if (friendEntity != null) {
            friendEntity.increaseDebt(charge);
            friendRepository.save(friendEntity);
            User user = userRepository.findOne(chargerToken);
            User friend = userRepository.findOne(payerToken);
            user.getBalanceCache().increaseDebt(charge);
            friend.getBalanceCache().increaseDue(charge);
            userRepository.save(user);
            userRepository.save(friend);
        }
    }


    public void refreshCache(String payerToken, String chargerToken, Double charge) {
        Friend friendEntity = friendRepository.findByUserIdAndFriendId(chargerToken,payerToken);
        friendEntity.setDebt(charge);
        friendRepository.save(friendEntity);
    }

    public void saveAll(List<User> users) {
        userRepository.save(users);
    }

    public List<Friend> getAllFriend() {
        return friendRepository.findAll();
    }

}
