package pl.darenie.dns.core.mapper

import pl.darenie.dns.jpa.BalanceCache
import pl.darenie.dns.jpa.User
import pl.darenie.dns.model.dto.UserDTO
import spock.lang.Specification

class UserMapperTest extends Specification {
    UserMapper userMapper = new UserMapper()

    void setup() {

    }

    def "map user to userDto"() {
        given:
            User user = Mock(User.class)
            user.email >> "w@wp.pl"
            user.firstname >> "Mati"
            user.lastname >> "Wydm"
            user.phoneNumber >>"123123123"
            user.birthDate >> new Date();
            user.balanceCache >> new BalanceCache(10, 10)
        when:
             def result = userMapper.mapToDto(user)
        then:
            result.email == "w@wp.pl"
            result.firstname == "Mati"
            result.lastname == "Wydm"
            result.phoneNumber ==  "123123123"

    }
}
