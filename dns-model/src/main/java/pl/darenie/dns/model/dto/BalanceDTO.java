package pl.darenie.dns.model.dto;

public class BalanceDTO {

    private Double debt;
    private Double due;
    private Double balance;

    public BalanceDTO(){}

    public BalanceDTO(Builder builder) {
        setDebt(builder.debt);
        setDue(builder.due);
        setBalance(builder.balance);
    }

    public BalanceDTO(Double debt, Double due) {
        this.debt = debt;
        this.due = due;
        this.balance = due - debt;
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

    public Double getBalance() {
        return (double) Math.round(balance*100)/100;
    }

    public void setBalance(Double balance) {
        this.balance = (double) Math.round(balance*100)/100;
    }


    public static final class Builder {
        private Double debt;
        private Double due;
        private Double balance;

        public Builder() {
        }

        public Builder debt(Double val) {
            debt = val;
            return this;
        }

        public Builder due(Double val) {
            due = val;
            return this;
        }

        public Builder balance(Double val) {
            balance = val;
            return this;
        }

        public BalanceDTO build() {
            return new BalanceDTO(this);
        }
    }
}
