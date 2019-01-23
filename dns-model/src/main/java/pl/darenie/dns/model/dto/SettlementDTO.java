package pl.darenie.dns.model.dto;

import pl.darenie.dns.model.enums.SettlementStatus;

public class SettlementDTO {

    private Long id;
    private Long billId;
    private UserDTO payer;
    private UserDTO charger;
    private String payerId;
    private String chargerId;
    private Double charge;
    private SettlementStatus status;

    public SettlementDTO(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
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

    public SettlementStatus getStatus() {
        return status;
    }

    public void setStatus(SettlementStatus status) {
        this.status = status;
    }

    private SettlementDTO(Builder builder) {
        id = builder.id;
        billId = builder.billId;
        payer = builder.payer;
        charger = builder.charger;
        payerId = builder.payerId;
        chargerId = builder.chargerId;
        charge = builder.charge;
        status = builder.status;
    }

    public UserDTO getCharger() {
        return charger;
    }

    public void setCharger(UserDTO charger) {
        this.charger = charger;
    }

    public UserDTO getPayer() {
        return payer;
    }

    public void setPayer(UserDTO payer) {
        this.payer = payer;
    }


    public static final class Builder {
        private Long id;
        private Long billId;
        private String payerId;
        private String chargerId;
        private Double charge;
        private SettlementStatus status;
        public UserDTO payer;
        public UserDTO charger;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder billId(Long val) {
            billId = val;
            return this;
        }

        public Builder payerId(String val) {
            payerId = val;
            return this;
        }

        public Builder payer(UserDTO val) {
            payer = val;
            return this;
        }

        public Builder charger(UserDTO val) {
            charger = val;
            return this;
        }

        public Builder chargerId(String val) {
            chargerId = val;
            return this;
        }

        public Builder charge(Double val) {
            charge = val;
            return this;
        }

        public Builder status(SettlementStatus val) {
            status = val;
            return this;
        }

        public SettlementDTO build() {
            return new SettlementDTO(this);
        }
    }
}
