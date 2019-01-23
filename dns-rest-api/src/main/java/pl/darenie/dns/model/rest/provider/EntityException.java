package pl.darenie.dns.model.rest.provider;

import pl.darenie.dns.model.enums.ErrorCode;

import java.util.List;

public class EntityException {

    private final String message;
    private final ErrorCode errorCode;
    private final List<String> parameters;

    public EntityException(String message, ErrorCode errorCode, List<String> parameters) {
        this.message = message;
        this.errorCode = errorCode;
        this.parameters = parameters;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public List<String> getParameters() {
        return parameters;
    }
}
