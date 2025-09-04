package co.com.crediya.model.solicitud.valueobjects;

import co.com.crediya.model.solicitud.exceptions.DomainValidationException;

import java.util.regex.Pattern;

public record Email(String value) {

    private static final Pattern emailRegex = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            Pattern.CASE_INSENSITIVE
    );
    public Email {
        validate(value);

    }
    public static void validate(String email) {

        if (!emailRegex.matcher(email).matches()) {
            throw new DomainValidationException("El email no tiene el formato correcto");
        }
    }

}