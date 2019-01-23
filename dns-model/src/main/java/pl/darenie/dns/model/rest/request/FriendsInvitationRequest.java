package pl.darenie.dns.model.rest.request;

import java.util.List;

public class FriendsInvitationRequest {

    private List<String> users;

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
