package pl.darenie.dns.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.darenie.dns.core.GroupController;
import pl.darenie.dns.core.firebase.FirebaseTokenHolder;
import pl.darenie.dns.model.rest.request.CreateGroupRequest;
import pl.darenie.dns.model.rest.request.GroupRequest;
import pl.darenie.dns.model.rest.response.GroupDetailsResponse;
import pl.darenie.dns.model.rest.response.GroupResponse;

import java.util.List;

@RestController
@RequestMapping("/group")
public class GroupResource {

    @Autowired
    private GroupController groupController;
    @Autowired
    private FirebaseTokenHolder firebaseTokenHolder;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseEntity createGroup(@RequestBody CreateGroupRequest createGroupRequest) {
        groupController.createGroup(createGroupRequest);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/add_user", method = RequestMethod.POST)
    public ResponseEntity addUserToGroup(@RequestBody GroupRequest groupRequest) {
        groupController.addUserToGroup(groupRequest);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/details", method = RequestMethod.POST)
    public ResponseEntity getGroupDetails(@RequestBody GroupRequest groupRequest) {
        GroupDetailsResponse groupResponse = groupController.getGroupDetails(groupRequest);
        return ResponseEntity.ok(groupResponse);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getUserGroups() {
        List<GroupResponse> groupResponseList = groupController.getUserGroups(firebaseTokenHolder.getUserToken());
        return ResponseEntity.ok(groupResponseList);
    }


}
