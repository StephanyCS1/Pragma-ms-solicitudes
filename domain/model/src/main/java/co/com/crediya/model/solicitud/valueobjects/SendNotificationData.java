package co.com.crediya.model.solicitud.valueobjects;

public record SendNotificationData(
        String requestId,
        String previousStatus,
        String newStatus,
        String email,
        String name
) {
}
