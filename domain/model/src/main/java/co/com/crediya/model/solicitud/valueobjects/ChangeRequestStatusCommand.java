package co.com.crediya.model.solicitud.valueobjects;

import java.util.UUID;

public record ChangeRequestStatusCommand(
        UUID requestId,
        UUID newStatusId
) {}