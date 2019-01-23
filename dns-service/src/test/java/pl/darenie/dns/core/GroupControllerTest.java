package pl.darenie.dns.core;

import org.junit.Before;
import org.junit.Test;
import pl.darenie.dns.core.mapper.GroupMapper;
import pl.darenie.dns.dao.*;
import pl.darenie.dns.jpa.Group;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.jpa.UserHasGroup;
import pl.darenie.dns.model.rest.request.CreateGroupRequest;
import pl.darenie.dns.model.rest.request.GroupRequest;

import java.util.Arrays;

import static org.mockito.Mockito.*;

public class GroupControllerTest {

    private UserRepository userRepository;
    private GroupRepository groupRepository;
    private UserHasGroupRepository userHasGroupRepository;
    private GroupMapper groupMapper;
    private GroupController groupController;

    @Before
    public void setUp() {
        userRepository = mock(UserRepository.class);
        groupRepository = mock(GroupRepository.class);
        userHasGroupRepository = mock(UserHasGroupRepository.class);
        groupMapper = mock(GroupMapper.class);
        groupController = new GroupController(userRepository, groupRepository, userHasGroupRepository, groupMapper);
    }

    @Test
    public void createGroupTest() {

        CreateGroupRequest rq = mock(CreateGroupRequest.class);

        when(rq.getName()).thenReturn("group-1");
        when(rq.getOwnerToken()).thenReturn("user-token-1");
        when(rq.getUserTokens()).thenReturn(Arrays.asList("user-token-1", "user-token-2", "user-token-3"));

        when(userRepository.findOne(anyString())).thenReturn(mock(User.class));
        groupController.createGroup(rq);

        verify(groupRepository, times(1)).save(any(Group.class));
    }

    @Test
    public void addUserToGroupTest() {

        GroupRequest rq = mock(GroupRequest.class);

        when(rq.getId()).thenReturn(1L);
        when(rq.getUserToken()).thenReturn("user-token-4");

        groupController.addUserToGroup(rq);

        verify(userHasGroupRepository, times(1)).save(any(UserHasGroup.class));
    }


}
