package pl.darenie.dns.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.jpa.UserHasDevice;

import java.util.List;

@Repository
public interface UserHasDeviceRepository extends JpaRepository<UserHasDevice, String> {

    List<UserHasDevice> findByUser(User user);

    List<UserHasDevice> findByUserToken(String userToken);

    @Query("UPDATE UserHasDevice uhd SET uhd.deviceToken = ?2 WHERE uhd.deviceToken = ?1")
    void refreshToken(String oldDeviceToken, String newDeviceToken);
}
