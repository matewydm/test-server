package pl.darenie.dns.model.dto;

import java.io.Serializable;
import java.util.Date;

public class UserDTO implements Serializable{

    private String firebaseToken;
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String birthDate;
    private String country;
    private String phoneNumber;
    private String facebookId;
    private BalanceDTO balance;

    protected UserDTO(Builder<?> builder) {
        setFirebaseToken(builder.firebaseToken);
        setEmail(builder.email);
        setFirstname(builder.firstname);
        setLastname(builder.lastname);
        setBirthDate(builder.birthDate);
        setPhoneNumber(builder.phoneNumber);
        setFacebookId(builder.facebookId);
        setBalance(builder.balance);
    }

    public UserDTO() {

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

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BalanceDTO getBalance() {
        return balance;
    }

    public void setBalance(BalanceDTO balance) {
        this.balance = balance;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getDisplayName(){
        return firstname + " " + lastname;
    }

    public static class Builder<T extends Builder<T>> {
        private String firebaseToken;
        private String email;
        private String firstname;
        private String lastname;
        private String birthDate;
        private String phoneNumber;
        private String country;
        public BalanceDTO balance;
        public String facebookId;

        public Builder() {
        }

        public T firebaseToken(String val) {
            firebaseToken = val;
            return (T) this;
        }

        public T email(String val) {
            email = val;
            return (T) this;
        }

        public T firstname(String val) {
            firstname = val;
            return (T) this;
        }

        public T lastname(String val) {
            lastname = val;
            return (T) this;
        }

        public T birthDate(String val) {
            birthDate = val;
            return (T) this;
        }

        public T phoneNumber(String val) {
            phoneNumber = val;
            return (T) this;
        }

        public T country(String val) {
            country = val;
            return (T) this;
        }

        public T facebookId(String val) {
            facebookId = val;
            return (T) this;
        }

        public T balance(BalanceDTO val) {
            balance = val;
            return (T) this;
        }

        public UserDTO build() {
            return new UserDTO((T) this);
        }
    }
}
