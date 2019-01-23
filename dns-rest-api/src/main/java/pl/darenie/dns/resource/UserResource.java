package pl.darenie.dns.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.darenie.dns.core.UserController;
import pl.darenie.dns.core.firebase.FirebaseTokenHolder;
import pl.darenie.dns.model.dto.FriendDTO;
import pl.darenie.dns.model.dto.UserDTO;
import pl.darenie.dns.model.enums.InvitationStatus;
import pl.darenie.dns.model.exception.CoreException;
import pl.darenie.dns.model.rest.request.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserResource {

    @Autowired
    private UserController userController;
    @Autowired
    private FirebaseTokenHolder firebaseTokenHolder;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getUserDTO() throws CoreException {
        UserDTO userDTO = userController.getOneDto(firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok(userDTO);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity register(@RequestBody UserDTO userDTO) throws CoreException {
        userController.registerUser(userDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/fb_register", method = RequestMethod.POST)
    public ResponseEntity fbRegister(@RequestBody UserDTO userDTO) throws CoreException {
        userController.updateUser(userDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity updateUser(@RequestBody UserDTO userDTO) throws CoreException {
        userController.updateUser(userDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/device/pair", method = RequestMethod.POST)
    public ResponseEntity pairDevice(@RequestBody DeviceTokenRequest deviceTokenRequest) throws CoreException {
        userController.updateDevice(firebaseTokenHolder.getUserToken(), deviceTokenRequest);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/device/separate", method = RequestMethod.POST)
    public ResponseEntity separateDevice(@RequestBody  DeviceTokenRequest deviceTokenRequest) throws CoreException {
        userController.deleteDevice(deviceTokenRequest);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/device/refresh", method = RequestMethod.DELETE)
    public ResponseEntity refreshDevice(@RequestBody DeviceRefreshRequest deviceRefreshRequest) throws CoreException {
        userController.refreshDevice(deviceRefreshRequest);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity getAll() {
        List<UserDTO> users = userController.getAllDTO();
        return ResponseEntity.ok(users);
    }

    @RequestMapping(value = "/friends", method = RequestMethod.GET)
    public ResponseEntity getFriendList() {
        List<UserDTO> friends = userController.getFriendsByToken(firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok(friends);
    }

    @RequestMapping(value = "/friend/{id}", method = RequestMethod.GET)
    public ResponseEntity getFriendDetails(@PathVariable("id") String friendToken) {
        FriendDTO friend = userController.getFriendDetails(firebaseTokenHolder.getUserToken(), friendToken);
        return ResponseEntity.ok(friend);
    }

    @RequestMapping(value = "/friend", method = RequestMethod.POST)
    public ResponseEntity postFriend(@RequestBody FriendDTO friendDTO) {
        userController.updateFriend(firebaseTokenHolder.getUserToken(), friendDTO);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/foreigners", method = RequestMethod.GET)
    public ResponseEntity getForeignerList() {
        List<UserDTO> foreigners = userController.getForeignersByToken(firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok(foreigners);
    }

    @RequestMapping(value = "/checks", method = RequestMethod.GET)
    public ResponseEntity getChecksList() {
        List<UserDTO> checks = userController.getChecksByToken(firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok(checks);
    }

    @RequestMapping(value = "/invitations", method = RequestMethod.GET)
    public ResponseEntity getInvitations() {
        List<UserDTO> invitations = userController.getInvitationsByReceiverToken(firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok(invitations);
    }

    @RequestMapping(value = "/invite", method = RequestMethod.POST)
    public ResponseEntity inviteFriend(@RequestBody FriendsInvitationRequest friendsInvitationRequest) throws CoreException{
        userController.inviteFriends(firebaseTokenHolder.getUserToken(), friendsInvitationRequest);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/invitation/accept", method = RequestMethod.POST)
    public ResponseEntity acceptInvitation(@RequestBody UserIdDTO userIdDTO) throws CoreException{
        userController.answerInvitation(firebaseTokenHolder.getUserToken(), userIdDTO.getUserId(), InvitationStatus.ACCEPTED);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/invitation/reject", method = RequestMethod.POST)
    public ResponseEntity rejectInvitation(@RequestBody UserIdDTO userIdDTO) throws CoreException{
        userController.answerInvitation(firebaseTokenHolder.getUserToken(), userIdDTO.getUserId(), InvitationStatus.REJECTED);
        return ResponseEntity.ok().build();
    }

}
