package pl.darenie.dns.model.firebase.notification;

public enum FcmErrorCode {

    MISSING_REGISTRATION_TOKEN ("MissingRegistration"),
    INVALID_REGISTRATION_TOKEN ("InvalidRegistration"),
    UNREGISTERED_DEVICE ("NotRegistered"),
    INVALID_PACKAGE_NAME ("InvalidPackageName"),
    AUTHENTICATION_ERROR ("AuthenticationError"),
    MISMATCHED_SENDER ("MismatchSenderId"),
    INVALID_JSON ("InvalidJSON"),
    INVALID_PARAMETERS ("InvalidParameters"),
    MESSAGE_TOO_BIG ("MessageTooBig"),
    INVALID_DATA_KEY ("InvalidDataKey"),
    INVALID_TIME_TO_LIVE ("InvalidTtl"),
    TIMEOUT ("Unavailable"),
    INTERNAL_SERVER_ERROR ("InternalServerError"),
    DEVICE_MESSAGE_RATE_EXCEEDED ("DeviceMessageRateExceeded"),
    TOPICS_MESSAGE_RATE_EXCEEDED ("TopicsMessageRateExceeded"),
    INVALID_APNS_CREDENTIALS ("InvalidApnsCredentials");

    private String errorCode;

    FcmErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
