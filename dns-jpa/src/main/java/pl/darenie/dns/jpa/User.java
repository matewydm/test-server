package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.TimestampEntity;
import pl.darenie.dns.model.enums.Role;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
public class User extends TimestampEntity {

    @Id
    @Column(name = "firebase_token", nullable = false, unique = true)
    private String firebaseToken;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @Column(name = "birth_date", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDate;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "facebook_id", unique = true)
    private String facebookId;

    @Column(name = "country")
    private String country;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<Friend> friends = new HashSet<>();

    @OneToMany(mappedBy="receiver", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<Invitation> receivedInvitations = new HashSet<>();

    @OneToMany(mappedBy="sender", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<Invitation> sentInvitations = new HashSet<>();

    @OneToMany(mappedBy="user", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<UserHasGroup> groups = new HashSet<>();

    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<Group> ownedGroups = new HashSet<>();

    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<Bill> ownedBills = new HashSet<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private BalanceCache balanceCache;

    public User(String firebaseToken, String email, String password, String firstname, String lastname, Date birthDate, String phoneNumber, String country, String facebookId, Role role, BalanceCache balanceCache) {
        this.firebaseToken = firebaseToken;
        this.email = email;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.role = role;
        this.facebookId = facebookId;
        this.balanceCache = balanceCache;
        this.balanceCache.setUser(this);
    }

    public User() {
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Set<Friend> getFriends() {
        return friends;
    }

    public void setFriends(Set<Friend> friends) {
        this.friends = friends;
    }

    public Set<Invitation> getReceivedInvitations() {
        return receivedInvitations;
    }

    public void setReceivedInvitations(Set<Invitation> receivedInvitations) {
        this.receivedInvitations = receivedInvitations;
    }

    public Set<UserHasGroup> getGroups() {
        return groups;
    }

    public void setGroups(Set<UserHasGroup> groups) {
        this.groups = groups;
    }

    public void addGroups(UserHasGroup group) {
        this.groups.add(group);
    }

    public Set<Group> getOwnedGroups() {
        return ownedGroups;
    }

    public void addOwnedGroups(Group ownedGroup) {
        this.ownedGroups.add(ownedGroup);
    }

    public Set<Invitation> getSentInvitations() {
        return sentInvitations;
    }

    public void setSentInvitations(Set<Invitation> sentInvitations) {
        this.sentInvitations = sentInvitations;
    }

    public String getDisplayName() {
        return firstname + " " + lastname;
    }

    public void setOwnedGroups(Set<Group> ownedGroups) {
        this.ownedGroups = ownedGroups;
    }

    public BalanceCache getBalanceCache() {
        return balanceCache;
    }

    public void setBalanceCache(BalanceCache balanceCache) {
        balanceCache.setUser(this);
        this.balanceCache = balanceCache;
    }

    public Set<Bill> getOwnedBills() {
        return ownedBills;
    }

    public void setOwnedBills(Set<Bill> ownedBills) {
        this.ownedBills = ownedBills;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }
}

