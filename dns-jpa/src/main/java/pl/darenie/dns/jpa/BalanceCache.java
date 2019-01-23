package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.TimestampEntity;

import javax.persistence.*;

@Entity
@Table(name = "balance_cache")
public class BalanceCache extends TimestampEntity{

    @Id
    @GeneratedValue(generator="balance_cache_seq")
    @SequenceGenerator(name="balance_cache_seq", sequenceName="balance_cache_SEQ", allocationSize=1)
    private Long id;

    @Column(name = "debt", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double debt;

    @Column(name = "due", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double due;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "firebase_token", unique = true, nullable = false)
    private User user;

    public BalanceCache() {
        debt = 0.0;
        due = 0.0;
    }

    public BalanceCache(Double debt, Double due) {
        this.debt = debt;
        this.due = due;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDebt() {
        return debt;
    }

    public void setDebt(Double debt) {
        this.debt = debt;
    }

    public Double getDue() {
        return due;
    }

    public void setDue(Double due) {
        this.due = due;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void increaseDebt(Double charge) {
        this.debt += charge;
    }

    public void increaseDue(Double charge) {
        this.debt += charge;
    }
}
