package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.TimestampEntity;

import javax.persistence.*;

@Entity
@Table(name = "user_has_device")
public class UserHasDevice extends TimestampEntity {

    @Id
    @Column(name = "device_token", unique = true, nullable = false)
    private String deviceToken;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private String userToken;

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUserToken() {
        return userToken;
    }
}
