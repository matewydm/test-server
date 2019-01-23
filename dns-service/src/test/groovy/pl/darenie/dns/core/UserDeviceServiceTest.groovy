package pl.darenie.dns.core

import pl.darenie.dns.dao.UserHasDeviceRepository
import pl.darenie.dns.jpa.User
import pl.darenie.dns.jpa.UserHasDevice
import spock.lang.Specification

class UserDeviceServiceTest extends Specification {

    UserDeviceService userDeviceService;
    UserHasDeviceRepository userHasDeviceRepository;

    void setup() {
        userHasDeviceRepository = Mock(UserHasDeviceRepository.class)
        userDeviceService = new UserDeviceService(userHasDeviceRepository)
    }

    def "refresh token"() {
        given:
        when:
        userDeviceService.checkTokenRefresh("token new", "token old");
        then:
        1 * userHasDeviceRepository.refreshToken("token new", "token old")
    }

    def "dot not refresh token"() {
        given:
        when:
        userDeviceService.checkTokenRefresh("token new", null);
        then:
        0 * userHasDeviceRepository.refreshToken(_, _)
    }

    def "separate user from device"() {
        given:
        def userHasDevice = new UserHasDevice()
        userHasDeviceRepository.findOne("token") >> userHasDevice
        when:
        userDeviceService.separateDeviceFromUser("token");
        then:
        1 * userHasDeviceRepository.save(userHasDevice)
        userHasDevice.deviceToken == "token"
        userHasDevice.user == null
    }


    def "pair user and device"(String deviceToken, String firebaseToken,String email) {
        given:
        def userHasDevice = new UserHasDevice()
        userHasDeviceRepository.findOne(deviceToken) >> userHasDevice
        def user = new User();
        user.firebaseToken = firebaseToken;
        user.email = email
        when:
        userDeviceService.pairUserAndDevice(user, deviceToken);

        then:
        1 * userHasDeviceRepository.save(userHasDevice)
        userHasDevice.deviceToken == deviceToken
        userHasDevice.user != null
        userHasDevice.user.firebaseToken == firebaseToken
        where:
        deviceToken     | firebaseToken | email
        "deviceToken 1" | "firebase deviceToken"|"test@wp.pl"
        "deviceToken 2" | "firebase deviceToken"|"test2@wp.pl"
        "deviceToken 3" | "firebase aaaa"|"test55@wp.pl"
        "deviceToken 66" | "firebase aaaa"|"dare@wp.pl"

    }
}


