package co.com.crediya.api.security;

import co.com.crediya.api.dto.NewRequest;
import co.com.crediya.api.dto.RequestDto;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class JwtHandler {

    public NewRequest toNewRequest(RequestDto request, Jwt jwt) {
        String email          = jwt.getClaimAsString("email");
        String identification = jwt.getClaimAsString("identification");
        UUID userId           = UUID.fromString(jwt.getClaimAsString("uid"));
        String name           = jwt.getClaimAsString("name");
        String lastName       = jwt.getClaimAsString("lastName");

        return new NewRequest(
                identification,
                name,
                lastName,
                email,
                request.phoneNumber(),
                request.requestedAmount(),
                request.loanTermMonths(),
                request.loanTypeId(),
                request.monthlyIncome(),
                userId
        );
    }

    public String toBearer(Jwt jwt) {
        return "Bearer " + jwt.getTokenValue();
    }

    public Mono<String> validateToken(ServerHttpRequest request) {
        var auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth == null || !auth.startsWith("Bearer ")) {
            return Mono.error(new DomainValidationException("Falta token de autorización"));
        }
        String bearer = auth.substring(7);
        if (bearer.isBlank()) {
            return Mono.error(new DomainValidationException("Token vacío"));
        }
        return Mono.just(bearer);
    }
}