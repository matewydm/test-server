package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.TimestampEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "`group`")
public class Group extends TimestampEntity {

    @Id
    @GeneratedValue(generator="group_seq")
    @SequenceGenerator(name="group_seq", sequenceName="group_SEQ", allocationSize=1)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "firebase_token", name = "owner_id")
    private User owner;

    @Column(name = "owner_id", insertable = false, updatable = false)
    private String ownerId;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group", orphanRemoval = true)
    private Set<UserHasGroup> userHasGroups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserHasGroup> getUserHasGroups() {
        return userHasGroups;
    }

    public void setUserHasGroups(Set<UserHasGroup> userHasGroups) {
        this.userHasGroups = userHasGroups;
    }

    public void addUserHasGroups(UserHasGroup userHasGroup) {
        this.userHasGroups.add(userHasGroup);
    }
}
