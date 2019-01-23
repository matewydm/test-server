package pl.darenie.dns.core.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.Friend;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.model.dto.FriendDTO;
import pl.darenie.dns.model.dto.UserDTO;

import java.text.SimpleDateFormat;

@Controller
public class FriendMapper {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    private CyclicAccountingMapper cyclicAccountingMapper;

    public FriendDTO mapToDto(Friend friend) {
        User user = friend.getFriend();
        return new FriendDTO.Builder()
                .firebaseToken(user.getFirebaseToken())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .birthDate(formatter.format(user.getBirthDate()))
                .phoneNumber(user.getPhoneNumber())
                .cyclicAccounting(cyclicAccountingMapper.mapToDto(friend.getCyclicAccounting()))
                .build();
    }

}
