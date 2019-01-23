package pl.darenie.dns.model.rest.response;

import pl.darenie.dns.model.dto.UserDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GroupDetailsResponse {

    private Long id;
    private String name;
    private UserDTO owner;
    private List<UserDTO> members;
    private Date creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public List<UserDTO> getMembers() {
        return members;
    }

    public void setMembers(List<UserDTO> members) {
        this.members = members;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void addMember(UserDTO member) {
        if (members == null) {
            members = new ArrayList<>();
        }
        this.members.add(member);
    }

}
