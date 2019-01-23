package pl.darenie.dns.model.rest.request;

public class UserCash {

    private String firebaseToken;
    private String firstname;
    private String lastname;
    private Double cash;

    public UserCash() {
    }

    public UserCash(String firebaseToken, Double cash) {
        this.firebaseToken = firebaseToken;
        this.cash = cash;
    }

    private UserCash(Builder builder) {
        setFirebaseToken(builder.firebaseToken);
        firstname = builder.firstname;
        lastname = builder.lastname;
        setCash(builder.cash);
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
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


    public static final class Builder {
        private String firebaseToken;
        private String firstname;
        private String lastname;
        private Double cash;

        public Builder() {
        }

        public Builder firebaseToken(String val) {
            firebaseToken = val;
            return this;
        }

        public Builder firstname(String val) {
            firstname = val;
            return this;
        }

        public Builder lastname(String val) {
            lastname = val;
            return this;
        }

        public Builder cash(Double val) {
            cash = val;
            return this;
        }

        public UserCash build() {
            return new UserCash(this);
        }
    }
}
