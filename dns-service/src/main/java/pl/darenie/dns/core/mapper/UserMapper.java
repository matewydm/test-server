package pl.darenie.dns.core.mapper;

import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.BalanceCache;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.model.dto.BalanceDTO;
import pl.darenie.dns.model.dto.UserDTO;
import pl.darenie.dns.model.enums.Role;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Controller
public class UserMapper {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public UserDTO mapToDto(User user) {
        BalanceCache balance = user.getBalanceCache();
        return new UserDTO.Builder()
                .firebaseToken(user.getFirebaseToken())
                .email(user.getEmail())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .birthDate(formatter.format(user.getBirthDate()))
                .phoneNumber(user.getPhoneNumber())
                .balance(new BalanceDTO(user.getBalanceCache().getDebt(), user.getBalanceCache().getDue()))
                .build();
    }

    public User mapToJpa(UserDTO userDTO)  {try {
            return new User(
                    userDTO.getFirebaseToken(),
                    userDTO.getEmail(),
                    UUID.randomUUID().toString(),
                    userDTO.getFirstname(),
                    userDTO.getLastname(),
                    formatter.parse(userDTO.getBirthDate()),
                    userDTO.getPhoneNumber(),
                    userDTO.getCountry(),
                    userDTO.getFacebookId(),
                    Role.USER,
                    new BalanceCache());
        } catch (ParseException e) {
            return new User(
                    userDTO.getFirebaseToken(),
                    userDTO.getEmail(),
                    UUID.randomUUID().toString(),
                    userDTO.getFirstname(),
                    userDTO.getLastname(),
                    new Date(),
                    userDTO.getPhoneNumber(),
                    userDTO.getCountry(),
                    userDTO.getFacebookId(),
                    Role.USER,
                    new BalanceCache());
        }
    }

    public User copyValues(User user, UserDTO userDTO) {
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setCountry(user.getCountry());
        try {
            user.setBirthDate(formatter.parse(userDTO.getBirthDate()));
        } catch (ParseException e) {
            user.setBirthDate(new Date());
        }
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setFacebookId(userDTO.getFacebookId());
        return user;
    }
}
