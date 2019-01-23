package pl.darenie.dns.model.exception;

import pl.darenie.dns.model.enums.ErrorCode;

import java.util.List;

public class CoreException extends Exception {
    private static final long serialVersionUID = -5287609441584763597L;
    private final ErrorCode errorCode;
    private List<String> parameters;


    public CoreException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public CoreException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CoreException(String message, ErrorCode errorCode, List<String> parameters) {
        super(message);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }

    public CoreException(String message, Throwable cause, ErrorCode errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CoreException(String message, Throwable cause, ErrorCode errorCode, List<String> parameters) {
        super(message, cause);
        this.errorCode = errorCode;
        this.parameters = parameters;
    }

    public CoreException(Throwable cause, ErrorCode errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
