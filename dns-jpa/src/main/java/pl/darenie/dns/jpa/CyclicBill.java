package pl.darenie.dns.jpa;

import pl.darenie.dns.jpa.superclass.CyclicEntity;
import javax.persistence.*;

@Entity
@Table(name = "cyclic_bill")
public class CyclicBill extends CyclicEntity {

    @Id
    @GeneratedValue(generator="cyclic_bill_seq")
    @SequenceGenerator(name="cyclic_bill_seq", sequenceName="cyclic_bill_SEQ", allocationSize=1)
    private Long id;

    @OneToOne
    @JoinColumn(name = "bill_id", referencedColumnName = "id", nullable = false)
    private Bill bill;

    @Column(name = "bill_id", nullable = false, insertable = false, updatable = false)
    private Long billId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
