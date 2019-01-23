package pl.darenie.dns.model.firebase.notification.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FcmMessageGroupResponse {

    /**
     * Amount of successfully sent group device messages
     */
    private Integer success;
    /**
     * Amount of failed group device messages sent
     */
    private Integer failure;
    /**
     * Failed registration_ids
     */
    @JsonProperty("failed_registration_ids")
    private List<String> failedRegistrationIds;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Integer getFailure() {
        return failure;
    }

    public void setFailure(Integer failure) {
        this.failure = failure;
    }

    public List<String> getFailedRegistrationIds() {
        return failedRegistrationIds;
    }

    public void setFailedRegistrationIds(List<String> failedRegistrationIds) {
        this.failedRegistrationIds = failedRegistrationIds;
    }
}
