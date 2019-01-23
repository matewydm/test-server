package pl.darenie.dns.model.dto;

import java.util.Comparator;

public class Accounter{

    private String token;
    private Double charge;
    private Double payment;

    public Accounter() {
        this.charge = 0.0;
        this.payment = 0.0;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Double getPayment() {
        return payment;
    }

    public void setPayment(Double payment) {
        this.payment = payment;
    }

    public Double getCharge() {
        return charge;
    }

    public void setCharge(Double charge) {
        this.charge = charge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Accounter accounter = (Accounter) o;

        return token != null ? token.equals(accounter.token) : accounter.token == null;
    }

    @Override
    public int hashCode() {
        return token != null ? token.hashCode() : 0;
    }

    public static class PayerComparator implements java.util.Comparator<Accounter>{

        @Override
        public int compare(Accounter o1, Accounter o2) {
            Double o1Diff = o1.getPayment()-o1.getCharge();
            Double o2Diff = o2.getPayment()-o2.getCharge();
            int value1 = o1Diff.compareTo(o2Diff);
            if (value1 == 0) {
                return (-1)*o1.getCharge().compareTo(o2.getCharge());
            }
            return value1;
        }
    }


    public static class ChargerComparator implements java.util.Comparator<Accounter>{

        @Override
        public int compare(Accounter o1, Accounter o2) {
            Double o1Diff = o1.getCharge()-o1.getPayment();
            Double o2Diff = o2.getCharge()-o2.getPayment();
            int value1 = o1Diff.compareTo(o2Diff);
            if (value1 == 0) {
                return (-1)*o1.getPayment().compareTo(o2.getPayment());
            }
            return value1;
        }
    }
}
