package pl.darenie.dns.model.enums;

public enum Role {

    USER, ANONYMOUS;

    public String getRole() {
        return "ROLE_" + this.toString();
    }
}
