package pl.darenie.dns.model.rest.request;

public class BillRequest {

    private Long id;
    private String ownerToken;
    private String payerToken;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    public String getPayerToken() {
        return payerToken;
    }

    public void setPayerToken(String payerToken) {
        this.payerToken = payerToken;
    }
}
