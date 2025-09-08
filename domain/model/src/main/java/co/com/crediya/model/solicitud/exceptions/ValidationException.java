package co.com.crediya.model.solicitud.exceptions;

public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public static final String INVALID_USER_DATA = "INVALID_USER_DATA";
    public static final String INVALID_UUID_FORMAT = "INVALID_UUID_FORMAT";
    public static final String INVALID_EMAIL_FORMAT = "INVALID_EMAIL_FORMAT";
    public static final String INVALID_DATE_FORMAT = "INVALID_DATE_FORMAT";
}