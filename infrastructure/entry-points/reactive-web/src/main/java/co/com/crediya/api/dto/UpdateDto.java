package co.com.crediya.api.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateDto(
        @NotNull(message = "El id de la solicitud es requerido")
        String requestId,
        @NotNull(message = "El nuevo estado de la solicitud es requerido")
        String newStatus
) {
}
