package co.com.crediya.model.solicitud.exceptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DomainValidationException extends RuntimeException {
    private final List<String> errors;

    public DomainValidationException(String message) {
        super(message);
        this.errors = message == null ?
                Collections.singletonList(null) :
                Collections.singletonList(message);
    }

    public DomainValidationException(List<String> errors) {
        super(String.join("; ", errors.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList())));
        this.errors = Collections.unmodifiableList(new ArrayList<>(errors));
    }

    public List<String> getErrors() {
        return errors;
    }
}