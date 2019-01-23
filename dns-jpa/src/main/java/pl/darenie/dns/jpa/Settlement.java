package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.TimestampEntity;
import pl.darenie.dns.model.enums.SettlementStatus;
import pl.darenie.dns.model.enums.Visibility;

import javax.persistence.*;

@Entity
@Table(name = "settlement")
public class Settlement extends TimestampEntity {

    @Id
    @GeneratedValue(generator="settlement_seq")
    @SequenceGenerator(name="settlement_seq", sequenceName="settlement_SEQ", allocationSize=1)
    private Long id;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "bill_id")
    private Bill bill;
        
    @Column(name = "bill_id", insertable = false, updatable = false)
    private Long billId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "firebase_token", name = "payer_id")
    private User payer;

    @Column(name = "payer_id", insertable = false, updatable = false)
    private String payerId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "firebase_token", name = "charger_id")
    private User charger;

    @Column(name = "charger_id", insertable = false, updatable = false)
    private String chargerId;

    @Column(name = "charge", nullable = false, columnDefinition = "DECIMAL(10,2)")
    private Double charge;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SettlementStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, columnDefinition = "VARCHAR(30) default 'VISIBLE'")
    private Visibility visibility;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SettlementStatus getStatus() {
        return status;
    }

    public void setStatus(SettlementStatus status) {
        this.status = status;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    
    public Bill getBill() {
        return bill;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public User getPayer() {
        return payer;
    }

    public void setPayer(User payer) {
        this.payer = payer;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public User getCharger() {
        return charger;
    }

    public void setCharger(User charger) {
        this.charger = charger;
    }

    public String getChargerId() {
        return chargerId;
    }

    public void setChargerId(String chargerId) {
        this.chargerId = chargerId;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    public Settlement() {}

    public Settlement(Bill bill, User payer, User charger, Double charge, SettlementStatus status) {
        this.bill = bill;
        this.payer = payer;
        this.charger = charger;
        this.charge = charge;
        this.status = status;
        this.visibility = Visibility.VISIBLE;
    }

    public Settlement(Bill bill, String payerId, String chargerId, Double charge, SettlementStatus status) {
        this.bill = bill;
        User payer = new User();
        payer.setFirebaseToken(payerId);
        this.payer = payer;
        User charger = new User();
        charger.setFirebaseToken(chargerId);
        this.charger = charger;
        this.charge = charge;
        this.status = status;
        this.visibility = Visibility.VISIBLE;
    }

    public Settlement(User payer, User charger, Double charge, SettlementStatus status) {
        this.payer = payer;
        this.charger = charger;
        this.charge = charge;
        this.status = status;
        this.visibility = Visibility.VISIBLE;
    }

    public Settlement(String payerId, String chargerId, Double charge, SettlementStatus status) {
        User payer = new User();
        payer.setFirebaseToken(payerId);
        this.payer = payer;
        User charger = new User();
        charger.setFirebaseToken(chargerId);
        this.charge = charge;
        this.status = status;
        this.visibility = Visibility.VISIBLE;
    }
}
