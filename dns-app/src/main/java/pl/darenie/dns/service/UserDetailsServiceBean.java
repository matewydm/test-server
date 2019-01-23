package pl.darenie.dns.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.dao.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service(value = UserDetailsServiceBean.NAME)
@Primary
public class UserDetailsServiceBean implements UserDetailsService {

    public static final String NAME = "userService";
    @Autowired
    private UserRepository userDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String tokenId) throws UsernameNotFoundException {
        User user = userDao.findOne(tokenId);
        if (user == null) {
            return null;
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getRole()));

        return new org.springframework.security.core.userdetails.User(user.getFirebaseToken(), user.getPassword(), grantedAuthorities);
    }
}
