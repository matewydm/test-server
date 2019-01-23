package pl.darenie.dns.model.firebase.notification.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FcmMessageSingleResponse implements Serializable{

    @JsonProperty("multicast_id")
    private Long multicastId;
    private Integer success;
    private Integer failure;
    @JsonProperty("canonical_ids")
    private Integer canonicalIds;
    private List<FcmResultObject> results;

    public Long getMulticastId() {
        return multicastId;
    }

    public void setMulticastId(Long multicastId) {
        this.multicastId = multicastId;
    }

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

    public Integer getCanonicalIds() {
        return canonicalIds;
    }

    public void setCanonicalIds(Integer canonicalIds) {
        this.canonicalIds = canonicalIds;
    }

    public List<FcmResultObject> getResults() {
        return results;
    }

    public void setResults(List<FcmResultObject> results) {
        this.results = results;
    }


}
