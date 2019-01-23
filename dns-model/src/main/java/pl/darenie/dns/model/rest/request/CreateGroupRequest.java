package pl.darenie.dns.model.rest.request;

import java.util.List;

public class CreateGroupRequest {

    private String name;
    private String ownerToken;
    private List<String> userTokens;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerToken() {
        return ownerToken;
    }

    public void setOwnerToken(String ownerToken) {
        this.ownerToken = ownerToken;
    }

    public List<String> getUserTokens() {
        return userTokens;
    }

    public void setUserTokens(List<String> userTokens) {
        this.userTokens = userTokens;
    }
}
