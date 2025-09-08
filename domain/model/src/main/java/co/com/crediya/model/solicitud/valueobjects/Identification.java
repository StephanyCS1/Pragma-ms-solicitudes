package co.com.crediya.model.solicitud.valueobjects;

import co.com.crediya.model.solicitud.exceptions.DomainValidationException;

public record Identification(String document) {

    public Identification {
        validate(document);
    }

    public static void validate(String document) {
        if (document == null || document.isBlank()) {
           throw new DomainValidationException("El documento de identidad es obligatorio");
        }
        if (document.length() < 5 || document.length() > 15) {
            throw new DomainValidationException("El documento debe tener entre 6 y 15 caracteres");
        }
        if (!document.matches("\\d+")) {
            throw new DomainValidationException("El documento solo debe contener números");
        }
    }
}