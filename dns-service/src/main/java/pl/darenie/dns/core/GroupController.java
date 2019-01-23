package pl.darenie.dns.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.Group;
import pl.darenie.dns.jpa.UserHasGroup;
import pl.darenie.dns.core.mapper.GroupMapper;
import pl.darenie.dns.dao.GroupRepository;
import pl.darenie.dns.dao.UserHasGroupRepository;
import pl.darenie.dns.dao.UserRepository;
import pl.darenie.dns.model.rest.request.CreateGroupRequest;
import pl.darenie.dns.model.rest.request.GroupRequest;
import pl.darenie.dns.model.rest.response.GroupDetailsResponse;
import pl.darenie.dns.model.rest.response.GroupResponse;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GroupController {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;
    private final UserHasGroupRepository userHasGroupRepository;
    private final GroupMapper groupMapper;

    @Autowired
    public GroupController(UserRepository userRepository, GroupRepository groupRepository, UserHasGroupRepository userHasGroupRepository, GroupMapper groupMapper) {
        this.userRepository = userRepository;
        this.groupRepository = groupRepository;
        this.userHasGroupRepository = userHasGroupRepository;
        this.groupMapper = groupMapper;
    }

    public void createGroup(CreateGroupRequest createGroupRequest) {
        Group group = new Group();
        group.setOwner(userRepository.findOne(createGroupRequest.getOwnerToken()));
        group.setName(createGroupRequest.getName());
        createGroupRequest.getUserTokens()
                .forEach(userToken -> group
                        .addUserHasGroups(
                                new UserHasGroup(group, userRepository.findOne(userToken))
                        )
                );
        group.addUserHasGroups(new UserHasGroup(group, userRepository.findOne(createGroupRequest.getOwnerToken())));
        groupRepository.save(group);
    }

    public void addUserToGroup(GroupRequest groupRequest) {
        UserHasGroup userHasGroup = new UserHasGroup(groupRequest.getId(),groupRequest.getUserToken());
        userHasGroupRepository.save(userHasGroup);
    }

    public List<GroupResponse> getUserGroups(String token) {
        List<UserHasGroup> userHasGroup = userHasGroupRepository.findByUserId(token);
        return userHasGroup.stream()
                .map(groupMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public GroupDetailsResponse getGroupDetails(GroupRequest groupRequest) {
        Group group = groupRepository.findOne(groupRequest.getId());
        return groupMapper.mapToDetailsResponse(group);
    }
}
