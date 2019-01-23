package pl.darenie.dns.test.db;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pl.darenie.dns.DnsApplication;
import pl.darenie.dns.dao.UserRepository;
import pl.darenie.dns.jpa.BalanceCache;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.model.enums.Role;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        initializers = ConfigFileApplicationContextInitializer.class,
        classes = {DnsApplication.class})
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.properties")
public class UserDatabaseTest {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUser() throws ParseException {
        userRepository.save(prepareUser());
        User users = userRepository.findOne("Token");
        assertThat(users.getFirstname()).isEqualTo("Imie");
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveUserNoEmailConstraint() throws ParseException {
        User user = prepareUser();
        user.setEmail(null);
        userRepository.save(user);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveUserNoFirstNameConstraint() throws ParseException {
        User user = prepareUser();
        user.setFirstname(null);
        userRepository.save(user);
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void saveUserNoTokenConstraint() throws ParseException {
        User user = prepareUser();
        user.setFirebaseToken(null);
        userRepository.save(user);
    }

    @Test
    public void removeUser() throws ParseException {
        userRepository.save(prepareUser());
        User userAdded = userRepository.findOne("Token");
        assertThat(userAdded.getFirstname()).isEqualTo("Imie");
        userRepository.delete(userAdded);
        User userDeleted = userRepository.findOne("Token");
        Assert.assertNull(userDeleted);
    }

    private User prepareUser() throws ParseException {
        return new User(
                "Token",
                "email",
                UUID.randomUUID().toString(),
                "Imie",
                "Nazwisko",
                formatter.parse("1995-09-25T00:00:00Z"),
                "505299959",
                "Poland",
                "null",
                Role.USER,
                new BalanceCache(0.0,0.0));
    }

}