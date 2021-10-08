package de.bachlorarbeit.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import de.bachlorarbeit.utility.DateTimeHelper;
import de.bachlorarbeit.utility.StringFormatHelper;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorDetail {

    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

    public ErrorDetail() {
        this.timestamp = DateTimeHelper.getNowISO();
    }

    public ErrorDetail(HttpStatus status, Exception e, HttpServletRequest request) {
        this();
        this.status = status.value();
        this.error = StringFormatHelper.formatEachWordUppercase(status.name());
        this.message = e.getMessage();
        this.path = request.getRequestURI();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String statusName) {
        this.error = StringFormatHelper.formatEachWordUppercase(statusName);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

