package pl.darenie.dns.core.firebase.notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.darenie.dns.jpa.User;
import pl.darenie.dns.jpa.UserHasDevice;
import pl.darenie.dns.core.UserDeviceService;
import pl.darenie.dns.model.firebase.notification.CategoryIdentifier;
import pl.darenie.dns.model.firebase.notification.FcmErrorCode;
import pl.darenie.dns.model.firebase.notification.FcmPriority;
import pl.darenie.dns.model.firebase.notification.request.FcmMessageRequest;
import pl.darenie.dns.model.firebase.notification.request.FcmNotification;
import pl.darenie.dns.model.firebase.notification.response.FcmMessageSingleResponse;
import pl.darenie.dns.model.firebase.notification.response.FcmMessageTopicResponse;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
public class FcmNotificationService {

    private static final Logger LOGGER = Logger.getLogger(FcmNotificationService.class.toString());
    @Autowired
    private FcmConnector fcmConnector;
    @Autowired
    private UserDeviceService userDevicesService;


    public FcmMessageSingleResponse sendSingleNotification(FcmMessageRequest request){
        LOGGER.log(Level.INFO, "Sending FCM single request");
        return fcmConnector.sendSingleNotification(request);
    }

    public FcmMessageTopicResponse sendTopicNotification(FcmMessageRequest request){
        LOGGER.log(Level.INFO, "Sending FCM topic request");
        return fcmConnector.sendTopicNotification(request);
    }

    public void sendSingleNotification(String token, String title, String body) {
        FcmNotification notification = new FcmNotification.Builder()
                .title(title)
                .body(body)
                .clickAction(CategoryIdentifier.NOTIFICATION.toString())
                .build();
        FcmMessageRequest request = new FcmMessageRequest.Builder()
                .to(token)
                .notification(notification)
                .priority(FcmPriority.HIGH)
                .build();
        FcmMessageSingleResponse response = sendSingleNotification(request);
        verifyResponse(request,response);
    }

    public boolean sendUserNotification(User user, String title, String content) {
        List<UserHasDevice> userDevices = userDevicesService.findDeviceTokens(user);
        return sendUserDevicesNotification(userDevices, title, content);
    }

    public boolean sendUserNotification(String userToken, String title, String content) {
        List<UserHasDevice> userDevices = userDevicesService.findDeviceTokens(userToken);
        return sendUserDevicesNotification(userDevices, title, content);
    }

    private boolean sendUserDevicesNotification (List<UserHasDevice> userDevices, String title, String content) {
        FcmNotification notification = new FcmNotification.Builder()
                .title(title)
                .body(content)
                .clickAction(CategoryIdentifier.NOTIFICATION.toString())
                .build();
        FcmMessageRequest.Builder builder = new FcmMessageRequest.Builder()
                .notification(notification)
                .priority(FcmPriority.HIGH);
        int notifiedDevices = 0;
        for (UserHasDevice device : userDevices) {
            FcmMessageRequest request = builder.to(device.getDeviceToken()).build();
            FcmMessageSingleResponse response = sendSingleNotification(request);
            if (verifyResponse(request, response)) {
                notifiedDevices++;
            }
        }
        return notifiedDevices > 0;
    }

    public boolean sendTopicNotification(String topic, String title, String body) {
        FcmNotification notification = new FcmNotification.Builder()
                .title(title)
                .body(body)
                .clickAction(CategoryIdentifier.NOTIFICATION.toString())
                .build();
        FcmMessageRequest request = new FcmMessageRequest.Builder()
                .topic(topic)
                .notification(notification)
                .priority(FcmPriority.HIGH)
                .build();
        FcmMessageTopicResponse response = sendTopicNotification(request);
        return verifyResponse(response);
    }

    public void clearMessagesQueue(String token) {
        if (token != null && !token.equals("")) {
            FcmMessageRequest request = new FcmMessageRequest.Builder().to(token).data("message", "clear_queue").priority(FcmPriority.HIGH).build();
            sendSingleNotification(request);
        }
    }

    private boolean verifyResponse(FcmMessageTopicResponse response) {
        return response.getMessageId() != null;
    }

    public boolean verifyResponse(FcmMessageRequest request, FcmMessageSingleResponse response) {
        if (response == null || response.getFailure() > 0) {
            LOGGER.log(Level.WARNING, "Message to " + request.getTo() + " wasn't delivered");
            if (response != null && response.getResults() != null) {
                response.getResults().forEach(r -> validDeviceToken(request.getTo(), r.getError()));
            }
            return false;
        } else {
            LOGGER.log(Level.INFO, "Notification to " + request.getTo() + " sent successfully");
            return true;
        }
    }

    private void validDeviceToken(String deviceToken, String errorCode) {
        LOGGER.log(Level.WARNING, "Message error code :  " + errorCode);
        if (errorCode != null && isDeviceOutdated(errorCode)){
            userDevicesService.removeDeviceToken(deviceToken);
        }
    }

    private boolean isDeviceOutdated(String errorCode) {
        return  errorCode.equals(FcmErrorCode.MISSING_REGISTRATION_TOKEN.getErrorCode()) ||
                errorCode.equals(FcmErrorCode.UNREGISTERED_DEVICE.getErrorCode()) ||
                errorCode.equals(FcmErrorCode.INVALID_REGISTRATION_TOKEN.getErrorCode());
    }

    public FcmMessageRequest buildAwakingNotification(String token) {
        return new FcmMessageRequest.Builder().to(token).data("message", "dare").priority(FcmPriority.NORMAL).build();
    }

}
