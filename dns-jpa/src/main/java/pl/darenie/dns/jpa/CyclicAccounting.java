package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.CyclicEntity;
import pl.darenie.dns.jpa.superclass.TimestampEntity;
import pl.darenie.dns.model.enums.CyclicType;

import javax.persistence.*;

@Entity
@Table(name = "cyclic_accounting")
public class CyclicAccounting extends CyclicEntity {

    @Id
    @GeneratedValue(generator="cyclic_accounting_seq")
    @SequenceGenerator(name="cyclic_accounting_seq", sequenceName="cyclic_accounting_SEQ", allocationSize=1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "friend_id", referencedColumnName = "id", nullable = false)
    private Friend friend;

    @Column(name = "friend_id", nullable = false, insertable = false, updatable = false)
    private Long friendId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public Long getFriendId() {
        return friendId;
    }

    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CyclicAccounting that = (CyclicAccounting) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        return friendId != null ? friendId.equals(that.friendId) : that.friendId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (friendId != null ? friendId.hashCode() : 0);
        return result;
    }
}
