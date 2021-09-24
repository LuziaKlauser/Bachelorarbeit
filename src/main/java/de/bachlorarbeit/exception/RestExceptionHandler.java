package de.bachlorarbeit.exception;

import de.bachlorarbeit.error.ErrorDetail;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(TableNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(TableNotFoundException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(HttpStatus.NOT_FOUND, ex, request);
        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers, HttpStatus status,
                                                                         WebRequest request) {

        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setStatus(HttpStatus.METHOD_NOT_ALLOWED.value());
        errorDetail.setError(HttpStatus.METHOD_NOT_ALLOWED.name());

        StringBuilder detailedMessage = new StringBuilder();
        detailedMessage.append(ex.getMethod());
        detailedMessage.append(
                " method is not supported for this request. Supported methods are [");
        for(int i = 0; i < Objects.requireNonNull(ex.getSupportedMethods()).length - 1; i++) {
            detailedMessage.append(ex.getSupportedMethods()[i]).append(", ");
        }
        detailedMessage.append(ex.getSupportedMethods()[ex.getSupportedMethods().length - 1]).append("]");

        errorDetail.setMessage(detailedMessage.toString());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.METHOD_NOT_ALLOWED);

    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
                                                               HttpStatus status, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setStatus(HttpStatus.NOT_FOUND.value());
        errorDetail.setError(HttpStatus.NOT_FOUND.name());
        errorDetail.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorDetail, null, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MultipartException.class)
    protected ResponseEntity<?> handleMultipartException(MultipartException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, ex, request);
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, ex, request);
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleInvalidParameterException(IllegalArgumentException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(HttpStatus.BAD_REQUEST, ex, request);
        return new ResponseEntity<>(errorDetail, null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<?> handleHttpClientErrorException(HttpClientErrorException ex, HttpServletRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(ex.getStatusCode(), ex, request);
        errorDetail.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorDetail, null, ex.getStatusCode());
    }
}

