package pl.darenie.dns.model.firebase.notification.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FcmMessageTopicResponse {

    /**
     * Failed topic name
     */
    private String error;
    /**
     * Successfully sent message_id
     */
    @JsonProperty("message_id")
    private Long messageId;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
