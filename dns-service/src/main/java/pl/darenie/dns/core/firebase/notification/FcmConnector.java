package pl.darenie.dns.core.firebase.notification;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.darenie.dns.model.firebase.notification.request.FcmMessageRequest;
import pl.darenie.dns.model.firebase.notification.response.FcmMessageSingleResponse;
import pl.darenie.dns.model.firebase.notification.response.FcmMessageTopicResponse;

import java.util.logging.Logger;

@Component
@Scope("singleton")
public class FcmConnector {
    private static final Logger LOGGER = Logger.getLogger(FcmConnector.class.toString());

    @Value("${firebase.fcm.api.url}")
    private String FCM_API_URL;
    @Value("${firebase.api.key}")
    private String LEGACY_SERVER_KEY;

    public FcmMessageSingleResponse sendSingleNotification(FcmMessageRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "key=" + LEGACY_SERVER_KEY);
        HttpEntity<FcmMessageRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<FcmMessageSingleResponse> response = restTemplate.postForEntity(FCM_API_URL, entity, FcmMessageSingleResponse.class);
        return response.getBody();
    }

    public FcmMessageTopicResponse sendTopicNotification(FcmMessageRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "key=" + LEGACY_SERVER_KEY);
        HttpEntity<FcmMessageRequest> entity = new HttpEntity<>(request, headers);
        ResponseEntity<FcmMessageTopicResponse> response = restTemplate.postForEntity(FCM_API_URL, entity, FcmMessageTopicResponse.class);
        return response.getBody();
    }
}
