package co.com.crediya.model.solicitud.valueobjects;

public record GeneralResponse<T>(
        int status,
        T data,
        Object error
) {}

