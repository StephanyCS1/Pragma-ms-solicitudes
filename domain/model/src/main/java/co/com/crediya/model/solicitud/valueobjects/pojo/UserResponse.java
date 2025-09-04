package co.com.crediya.model.solicitud.valueobjects.pojo;


public record UserResponse(
        String id,
        String name,
        String lastName,
        String email,
        Double baseSalary
) {
}