package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.TimestampEntity;
import pl.darenie.dns.model.enums.UserCashType;

import javax.persistence.*;

@Entity
@Table(name = "user_has_cash_bill",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","bill_id","type"})
)
public class UserHasCashBill extends TimestampEntity {

    @Id
    @GeneratedValue(generator="settlement_seq")
    @SequenceGenerator(name="settlement_seq", sequenceName="settlement_SEQ", allocationSize=1)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "firebase_token", name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "bill_id", nullable = false)
    private Bill bill;

    @Column(name = "user_id", insertable = false, updatable = false, nullable = false)
    private String userId;

    @Column(name = "bill_id", insertable = false, updatable = false, nullable = false)
    private Long billId;

    @Column(name = "cash", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double cash;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserCashType type;

    public UserHasCashBill() {}

    public UserHasCashBill(Bill bill, User user, Double cash, UserCashType type) {
        setBill(bill);
        setUser(user);
        setUserId(user.getFirebaseToken());
        setCash(cash);
        setType(type);
    }

    public UserHasCashBill(Bill bill, String userId, Double cash, UserCashType type) {
        setBill(bill);
        setBillId(bill.getId());
        User user = new User();
        user.setFirebaseToken(userId);
        setUser(user);
        setUserId(user.getFirebaseToken());
        setCash(cash);
        setType(type);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
    }

    public UserCashType getType() {
        return type;
    }

    public void setType(UserCashType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserHasCashBill that = (UserHasCashBill) o;

//        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        if (billId != null ? !billId.equals(that.billId) : that.billId != null) return false;
        return type == that.type;
    }

//    @Override
//    public int hashCode() {
//        int result = id != null ? id.hashCode() : 0;
//        result = 31 * result + (userId != null ? userId.hashCode() : 0);
//        result = 31 * result + (billId != null ? billId.hashCode() : 0);
//        result = 31 * result + (type != null ? type.hashCode() : 0);
//        return result;
//    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (billId != null ? billId.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
