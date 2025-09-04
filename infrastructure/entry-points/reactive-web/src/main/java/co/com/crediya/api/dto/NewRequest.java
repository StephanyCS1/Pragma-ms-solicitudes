package co.com.crediya.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.UUID;

public record NewRequest(
        @NotBlank(message = "El documento es requerido")
        @Size(min = 5, max = 15, message = "El documento debe tener entre 5 y 15 caracteres")
        String documentNumber,

        @NotBlank(message = "El nombre es requerido")
        @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
        String firstName,

        @NotBlank(message = "El apellido es requerido")
        @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
        String lastName,

        @NotBlank(message = "El email es requerido")
        @Email(message = "Formato de email inválido")
        String email,

        @NotBlank(message = "El teléfono es requerido")
        String phoneNumber,

        @NotNull(message = "El monto solicitado es requerido")
        @Positive(message = "El monto debe ser mayor a 0")
        BigDecimal requestedAmount,

        @NotNull(message = "El plazo del crédito es requerido")
        @Min(value = 6, message = "El plazo debe ser mínimo 6 meses")
        @Max(value = 360, message = "El plazo no puede exceder 360 meses")
        Integer loanTermMonths,

        @NotNull(message = "El tipo de crédito es requerido")
        UUID loanTypeId,

        @NotNull(message = "Los ingresos mensuales son requeridos")
        @Positive(message = "Los ingresos deben ser mayores a 0")
        BigDecimal monthlyIncome
) {}