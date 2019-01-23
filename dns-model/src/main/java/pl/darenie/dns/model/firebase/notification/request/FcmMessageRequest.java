package pl.darenie.dns.model.firebase.notification.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import pl.darenie.dns.model.firebase.notification.FcmPriority;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
//@XmlAccessorType(XmlAccessType.FIELD)
//@XmlType
public class FcmMessageRequest implements Serializable {

    private static final String COLLAPSE_KEY = "COLLAPSE_MESSAGE";

    private Map<String,String> data;
    private String to;
    private String priority;
    @JsonProperty("notification")
    private FcmNotification notification;
    @JsonProperty("collapse_key")
    private String collapseKey;
    @JsonProperty("time_to_live")
    private String timeToLive;

    public FcmMessageRequest(){}

    private FcmMessageRequest(Builder builder) {
        this.data = builder.data;
        this.to = builder.to;
        this.priority = builder.priority;
        this.notification = builder.notification;
        this.collapseKey = builder.collapseKey;
        this.timeToLive = builder.timeToLive;
    }

    public String getCollapseKey() {
        return collapseKey;
    }

    public String getTimeToLive() {
        return timeToLive;
    }

    public Map<String, String> getData() {
        return data;
    }

    public String getTo() {
        return to;
    }

    public String getPriority() {
        return priority;
    }

    public FcmNotification getNotification() {
        return notification;
    }

    public static class Builder {

        private static final String IDENTIFIER_KEY = "identifier";
        private Map<String,String> data;
        private String to;
        private String priority;
        private FcmNotification notification;
        private String collapseKey;
        private String timeToLive;
        private boolean isTopic;

        public Builder topic(String to) {
            this.to = to;
            isTopic = true;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            isTopic = false;
            return this;
        }

        public Builder data(String key, String value) {
            if (data == null) {
                data = new HashMap<>();
            }
            data.put(key,value);
            return this;
        }

        public Builder priority(FcmPriority priority) {
            this.priority = priority.toString();
            return this;
        }

        public Builder notification(FcmNotification notification) {
            this.notification = notification;
            return this;
        }

        public Builder timeToLive(String timeToLive) {
            this.timeToLive = timeToLive;
            return this;
        }

        public Builder collapseKey() {
            if (!isTopic) {
                this.collapseKey = COLLAPSE_KEY;
            }
            return this;
        }

        public FcmMessageRequest build() {
            collapseKey();
            return new FcmMessageRequest(this);
        }

    }
}