package pl.darenie.dns.model.dto;

import pl.darenie.dns.model.enums.BillPriority;
import pl.darenie.dns.model.rest.request.UserCash;

import java.util.List;

public class BillDTO {

    private Long id;
    private String name;
    private Double payment;
    private BillPriority priority;
    private List<UserCash> payers;
    private List<UserCash> chargers;
    private String ownerId;
    private CyclicBillDTO cyclicBill;

    public BillDTO(){}

    private BillDTO(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setPayment(builder.payment);
        setPriority(builder.priority);
        setPayers(builder.payers);
        setChargers(builder.chargers);
        setOwnerId(builder.ownerId);
        setCyclicBill(builder.cyclicBill);
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

    public List<UserCash> getChargers() {
        return chargers;
    }

    public void setChargers(List<UserCash> chargers) {
        this.chargers = chargers;
    }

    public List<UserCash> getPayers() {
        return payers;
    }

    public void setPayers(List<UserCash> payers) {
        this.payers = payers;
    }

    public CyclicBillDTO getCyclicBill() {
        return cyclicBill;
    }

    public void setCyclicBill(CyclicBillDTO cyclicBill) {
        this.cyclicBill = cyclicBill;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static final class Builder {
        private Long id;
        private String name;
        private Double payment;
        private BillPriority priority;
        private List<UserCash> payers;
        private List<UserCash> chargers;
        private String ownerId;
        private CyclicBillDTO cyclicBill;

        public Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder payment(Double val) {
            payment = val;
            return this;
        }

        public Builder priority(BillPriority val) {
            priority = val;
            return this;
        }

        public Builder payers(List<UserCash> val) {
            payers = val;
            return this;
        }

        public Builder chargers(List<UserCash> val) {
            chargers = val;
            return this;
        }

        public Builder ownerId(String val) {
            ownerId = val;
            return this;
        }

        public Builder cyclicBill(CyclicBillDTO val) {
            cyclicBill = val;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public BillDTO build() {
            return new BillDTO(this);
        }

    }
}
