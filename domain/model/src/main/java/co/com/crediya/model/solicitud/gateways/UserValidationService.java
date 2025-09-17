package co.com.crediya.model.solicitud.gateways;

import co.com.crediya.model.solicitud.valueobjects.pojo.UserResponse;
import reactor.core.publisher.Mono;

public interface UserValidationService {
    Mono<UserResponse> findByEmail(String email, String token);

}
