package pl.darenie.dns.model.firebase.notification.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class FcmNotification implements Serializable {

    private String title;
    private String sound;
    private String body;
    @JsonProperty("click_action")
    private String clickAction;


    public FcmNotification(Builder builder) {
        this.title = builder.title;
        this.sound = builder.sound;
        this.body = builder.body;
        this.clickAction = builder.clickAction;
    }

    public String getSound() {
        return sound;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public String getClickAction() {
        return clickAction;
    }

    public static class Builder {

        private String title;
        private String sound;
        private String body;
        private String clickAction;

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder sound(String sound) {
            this.sound = sound;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder clickAction(String categoryIdentifier) {
            this.clickAction = categoryIdentifier;
            return this;
        }


        public FcmNotification build() {
            return new FcmNotification(this);
        }

    }
}
