package pl.darenie.dns.core.firebase;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
public class FirebaseTokenHolder {

    public boolean isUserLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public String getUserToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && isUserLoggedIn()){
            Object principal = authentication.getPrincipal();
            if (principal != null) {
                return ((User) principal).getUsername();
            }
        }
        return null;
    }
}
