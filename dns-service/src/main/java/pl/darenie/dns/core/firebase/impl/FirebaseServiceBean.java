package pl.darenie.dns.core.firebase.impl;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.tasks.Task;
import com.google.firebase.tasks.Tasks;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import pl.darenie.dns.core.firebase.FirebaseService;

@Service
public class FirebaseServiceBean implements FirebaseService {
    @Override
    public FirebaseToken parseToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("FirebaseTokenBlank");
        }
        try {
            Task<FirebaseToken> authTask = FirebaseAuth.getInstance().verifyIdToken(token);

            Tasks.await(authTask);

            return authTask.getResult();
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }
}
