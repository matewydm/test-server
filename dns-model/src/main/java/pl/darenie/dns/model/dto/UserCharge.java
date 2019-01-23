package pl.darenie.dns.model.dto;

public class UserCharge {

    private Double charge;
    private UserDTO payer;

    public UserCharge(UserDTO userDTO, Double charge) {
        this.charge = charge;
        this.payer = userDTO;
    }

    public UserDTO getPayer() {
        return payer;
    }

    public void setPayer(UserDTO payer) {
        this.payer = payer;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }
}
