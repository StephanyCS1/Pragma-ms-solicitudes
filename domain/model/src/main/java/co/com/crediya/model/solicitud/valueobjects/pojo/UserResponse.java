package co.com.crediya.model.solicitud.valueobjects.pojo;


public record UserResponse(
        String uid,
        String name,
        String lastName,
        String email,
        Double baseSalary,
        String identification,
        String rol
) {
}