package pl.darenie.dns.core.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.Group;
import pl.darenie.dns.jpa.UserHasGroup;
import pl.darenie.dns.model.rest.response.GroupDetailsResponse;
import pl.darenie.dns.model.rest.response.GroupResponse;

@Controller
public class GroupMapper {

    @Autowired
    private UserMapper userMapper;

    public GroupDetailsResponse mapToDetailsResponse(Group group) {
        GroupDetailsResponse groupResponse = new GroupDetailsResponse();
        groupResponse.setId(group.getId());
        groupResponse.setName(group.getName());
        groupResponse.setOwner(userMapper.mapToDto(group.getOwner()));
        group.getUserHasGroups().
                forEach(userHasGroup ->
                        groupResponse.addMember(userMapper.mapToDto(userHasGroup.getUser()))
                );
        return groupResponse;
    }

    public GroupResponse mapToResponse(UserHasGroup userHasGroup) {
        GroupResponse groupResponse = new GroupResponse();
        groupResponse.setId(userHasGroup.getGroupId());
        groupResponse.setName(userHasGroup.getGroup().getName());
        groupResponse.setOwnerToken(userHasGroup.getGroup().getOwner().getFirebaseToken());
        return groupResponse;
    }

}
