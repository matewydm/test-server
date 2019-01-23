package pl.darenie.dns.model.rest.provider;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.darenie.dns.model.exception.CoreException;

@ControllerAdvice
public class CoreRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ Exception.class })
    public ResponseEntity handleAll(CoreException e, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EntityException(e.getMessage(), e.getErrorCode(), e.getParameters()));
    }
}
