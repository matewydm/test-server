package pl.darenie.dns.jpa.embeddable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserFriendId implements Serializable {

    @Column(name = "user_token", nullable = false)
    private String userId;

    @Column(name = "friend_token", nullable = false)
    private String friendId;

    public UserFriendId() {}

    public UserFriendId(String userId, String friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFriendId() {
        return friendId;
    }

    public void setFriendId(String friendId) {
        this.friendId = friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserFriendId that = (UserFriendId) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return friendId != null ? friendId.equals(that.friendId) : that.friendId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = result + (friendId != null ? friendId.hashCode() : 0);
        return result;
    }
}
