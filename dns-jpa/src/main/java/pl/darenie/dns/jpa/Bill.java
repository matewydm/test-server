package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.TimestampEntity;
import pl.darenie.dns.model.enums.BillPriority;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bill")
@NamedEntityGraph(name = "Bill.userHasCashBill",
        attributeNodes = @NamedAttributeNode("userHasCashBill"))
public class Bill extends TimestampEntity {

    @Id
    @GeneratedValue(generator="bill_seq")
    @SequenceGenerator(name="bill_seq", sequenceName="bill_SEQ", allocationSize=1)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "payment", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double payment;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "firebase_token", nullable = false)
    private User owner;

    @Column(name = "owner_id", insertable = false, updatable = false, nullable = false)
    private String ownerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private BillPriority priority;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill", orphanRemoval = true)
    private Set<UserHasCashBill> userHasCashBill = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "bill", orphanRemoval = true)
    private Set<Settlement> settlements = new HashSet<>();

    @OneToOne(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private CyclicBill cyclicBill;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public BillPriority getPriority() {
        return priority;
    }

    public void setPriority(BillPriority priority) {
        this.priority = priority;
    }

    public Set<UserHasCashBill> getUserHasCashBill() {
        return userHasCashBill;
    }

    public void setUserHasCashBill(Set<UserHasCashBill> userHasCashBill) {
        this.userHasCashBill = userHasCashBill;
    }

    public void addUserHasCashBill(UserHasCashBill userHasChargeBill) {
        this.userHasCashBill.add(userHasChargeBill);
    }

    public Set<Settlement> getSettlements() {
        return settlements;
    }

    public void setSettlements(Set<Settlement> settlements) {
        this.settlements = settlements;
    }

    public void addSettlement(Settlement settlement) {
        this.settlements.add(settlement);
    }

    public CyclicBill getCyclicBill() {
        return cyclicBill;
    }

    public void setCyclicBill(CyclicBill cyclicBill) {
        if (cyclicBill != null) {
            cyclicBill.setBill(this);
        }
        this.cyclicBill = cyclicBill;
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
        this.owner = new User();
        this.owner.setFirebaseToken(ownerId);
        this.ownerId = ownerId;
    }
}
