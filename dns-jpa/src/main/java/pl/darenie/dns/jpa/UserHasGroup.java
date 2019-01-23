package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.embeddable.UserGroupId;
import pl.darenie.dns.jpa.superclass.TimestampEntity;

import javax.persistence.*;

@Entity
@Table(name = "user_has_group")
public class UserHasGroup extends TimestampEntity {

    @EmbeddedId
    private UserGroupId userGroupId = new UserGroupId();

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(referencedColumnName = "firebase_token", name = "user_id")
    private User user;

    @Column(name = "user_id", insertable = false, updatable = false)
    private String userId;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(referencedColumnName = "id", name = "group_id")
    private Group group;

    @Column(name = "group_id", insertable = false, updatable = false)
    private Long groupId;

    public UserHasGroup() {}

    public UserHasGroup(Group group, User user) {
        UserGroupId userGroupId = new UserGroupId();
        userGroupId.setGroupId(group.getId());
        userGroupId.setUserId(user.getFirebaseToken());
        setGroup(group);
        setUser(user);
    }

    public UserHasGroup(Long groupId, String userToken) {
        group = new Group();
        group.setId(groupId);
        user = new User();
        user.setFirebaseToken(userToken);
        UserGroupId userGroupId = new UserGroupId();
        userGroupId.setGroupId(group.getId());
        userGroupId.setUserId(user.getFirebaseToken());
    }

    public UserGroupId getUserGroupId() {
        return userGroupId;
    }

    public void setUserGroupId(UserGroupId userGroupId) {
        this.userGroupId = userGroupId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
