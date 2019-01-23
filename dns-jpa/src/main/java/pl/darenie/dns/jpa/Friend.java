package pl.darenie.dns.jpa;


import javax.persistence.*;

@Entity
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(generator="friend_seq")
    @SequenceGenerator(name="friend_seq", sequenceName="friend_SEQ", allocationSize=1)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "firebase_token", name = "user_token", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "firebase_token", name = "friend_token", nullable = false)
    private User friend;

    @Column(name = "user_token", insertable = false, updatable = false, nullable = false)
    private String userId;

    @Column(name = "friend_token", insertable = false, updatable = false, nullable = false)
    private String friendId;

    @Column(name = "debt", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double debt;

    @OneToOne(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
    private CyclicAccounting cyclicAccounting;

    public Friend() {}

    public Friend(User user, User friend) {
        this.user = user;
        this.friend = friend;
        debt = 0.0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CyclicAccounting getCyclicAccounting() {
        return cyclicAccounting;
    }

    public void setCyclicAccounting(CyclicAccounting cyclicAccounting) {
        if (cyclicAccounting != null) {
            cyclicAccounting.setFriend(this);
        }
        this.cyclicAccounting = cyclicAccounting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Friend friend = (Friend) o;

        if (userId != null ? !userId.equals(friend.userId) : friend.userId != null) return false;
        return friendId != null ? friendId.equals(friend.friendId) : friend.friendId == null;
    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (friendId != null ? friendId.hashCode() : 0);
        return result;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public void increaseDebt(Double charge) {
        this.debt += charge;
    }
}

