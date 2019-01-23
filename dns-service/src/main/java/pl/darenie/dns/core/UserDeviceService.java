package pl.darenie.dns.core;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.jpa.UserHasDevice;
import pl.darenie.dns.dao.UserHasDeviceRepository;

import java.util.List;

@Controller
public class UserDeviceService {

    private static final Logger LOGGER = Logger.getLogger(UserDeviceService.class.toString());

    private final UserHasDeviceRepository userHasDeviceRepository;

    @Autowired
    public UserDeviceService(UserHasDeviceRepository userHasDeviceRepository) {
        this.userHasDeviceRepository = userHasDeviceRepository;
    }

    public List<UserHasDevice> findDeviceTokens(User user) {
        return userHasDeviceRepository.findByUser(user);
    }

    public List<UserHasDevice> findDeviceTokens(String userId) {
        return userHasDeviceRepository.findByUserToken(userId);
    }

    public void pairUserAndDevice(User user, String deviceToken) {

        if (deviceToken != null && !deviceToken.equals("")) {
            UserHasDevice userDevices = prepareUserDevice(deviceToken);
            if (userDevices.getDeviceToken() == null) {
                userDevices.setDeviceToken(deviceToken);
            }
            userDevices.setUser(user);

            userHasDeviceRepository.save(userDevices);
        }
    }

    public void checkTokenRefresh(String oldDeviceToken, String deviceToken) {
        if (oldDeviceToken != null && deviceToken != null) {
            try {
                userHasDeviceRepository.refreshToken(oldDeviceToken,deviceToken);
            } catch (Exception e) {
                LOGGER.log(Logger.Level.WARN, "Cannot swap tokens : " + deviceToken);
            }
        }
    }

    public void separateDeviceFromUser(String deviceToken) {

        if (deviceToken != null && !deviceToken.equals("")) {
            UserHasDevice userDevices = prepareUserDevice(deviceToken);
            userDevices.setDeviceToken(deviceToken);
            userDevices.setUser(null);
            userHasDeviceRepository.save(userDevices);
        }
    }

    private UserHasDevice prepareUserDevice(String deviceToken) {
        UserHasDevice userDevices = userHasDeviceRepository.findOne(deviceToken);
        if (userDevices == null) {
            userDevices = new UserHasDevice();
        }
        return userDevices;
    }

    public void removeDeviceToken(String deviceToken) {
        if (deviceToken != null) {
            LOGGER.log(Logger.Level.WARN, "Removing device token : " + deviceToken);
            userHasDeviceRepository.delete(deviceToken);
        }
    }
}
