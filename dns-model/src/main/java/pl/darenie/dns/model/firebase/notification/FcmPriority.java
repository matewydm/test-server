package pl.darenie.dns.model.firebase.notification;

public enum FcmPriority {

    NORMAL("normal"), HIGH("high");

    String value;

    FcmPriority(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

}
