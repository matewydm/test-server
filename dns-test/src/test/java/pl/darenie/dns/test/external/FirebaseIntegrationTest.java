package pl.darenie.dns.test.external;

import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import pl.darenie.dns.model.firebase.notification.CategoryIdentifier;
import pl.darenie.dns.model.firebase.notification.FcmPriority;
import pl.darenie.dns.model.firebase.notification.request.FcmMessageRequest;
import pl.darenie.dns.model.firebase.notification.request.FcmNotification;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application.properties")
public class FirebaseIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    @Value("${firebase.fcm.api.url}")
    private String FCM_API_URL;
    @Value("${firebase.api.key}")
    private String LEGACY_SERVER_KEY;

    @Test
    public void checkFcmStatus() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + LEGACY_SERVER_KEY);

        FcmMessageRequest request = new FcmMessageRequest.Builder()
                .to("token1")
                .data("key","value")
                .priority(FcmPriority.HIGH)
                .notification(new FcmNotification.Builder()
                        .body("test")
                        .title("test_title")
                        .clickAction(CategoryIdentifier.NOTIFICATION.toString())
                        .build())
                .build();

        HttpEntity<FcmMessageRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity( FCM_API_URL , entity , String.class );

        Assert.assertEquals(
                response.getStatusCode().value(),
                HttpStatus.SC_OK);
    }

    @Test
    public void checkTopicFcmStatus() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + LEGACY_SERVER_KEY);

        FcmMessageRequest request = new FcmMessageRequest.Builder()
                .topic("topic1")
                .data("key","value")
                .priority(FcmPriority.HIGH)
                .notification(new FcmNotification.Builder()
                        .body("test")
                        .title("test_title")
                        .clickAction(CategoryIdentifier.NOTIFICATION.toString())
                        .build())
                .build();

        HttpEntity<FcmMessageRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity( FCM_API_URL , entity , String.class );

        Assert.assertEquals(
                response.getStatusCode().value(),
                HttpStatus.SC_OK);
    }

    @Test
    public void checkErrorFcmStatus() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + LEGACY_SERVER_KEY);

        FcmMessageRequest request = new FcmMessageRequest.Builder()
                .data("key","value")
                .priority(FcmPriority.HIGH)
                .notification(new FcmNotification.Builder()
                        .body("test")
                        .title("test_title")
                        .clickAction(CategoryIdentifier.NOTIFICATION.toString())
                        .build())
                .build();

        HttpEntity<FcmMessageRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<String> response = testRestTemplate.postForEntity( FCM_API_URL , entity , String.class );

        Assert.assertEquals(
                response.getStatusCode().value(),
                HttpStatus.SC_BAD_REQUEST);
    }
}
