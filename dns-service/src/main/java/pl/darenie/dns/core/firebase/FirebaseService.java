package pl.darenie.dns.core.firebase;

import com.google.firebase.auth.FirebaseToken;

public interface FirebaseService {

    FirebaseToken parseToken(String token);
}
