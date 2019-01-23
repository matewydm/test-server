package pl.darenie.dns.core.firebase.authorization;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import org.springframework.stereotype.Component;
import pl.darenie.dns.model.dto.UserDTO;
import pl.darenie.dns.model.enums.ErrorCode;
import pl.darenie.dns.model.exception.CoreException;

import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

@Component
public class FirebaseUserService {

    private Logger logger = Logger.getLogger(FirebaseUserService.class.getName());

    public UserRecord registerFirebaseUser(UserDTO userDTO) throws CoreException {
        UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                .setEmail(userDTO.getEmail())
                .setEmailVerified(false)
                .setPassword(userDTO.getPassword())
                .setDisplayName(getDisplayName(userDTO))
                .setDisabled(false);

        try {
            return FirebaseAuth.getInstance().createUserAsync(request).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new CoreException("Cannot add new user to firebase", ErrorCode.FIREBASE_CREATE_USER_ERROR);
        }
    }

    private String getDisplayName(UserDTO userDTO) {
        return userDTO.getFirstname() + userDTO.getLastname();
    }
}
