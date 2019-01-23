package pl.darenie.dns.model.rest.request;

public class DeviceRefreshRequest {

    private String oldDeviceToken;
    private String newDeviceToken;

    public String getOldDeviceToken() {
        return oldDeviceToken;
    }

    public void setOldDeviceToken(String oldDeviceToken) {
        this.oldDeviceToken = oldDeviceToken;
    }

    public String getNewDeviceToken() {
        return newDeviceToken;
    }

    public void setNewDeviceToken(String newDeviceToken) {
        this.newDeviceToken = newDeviceToken;
    }
}
