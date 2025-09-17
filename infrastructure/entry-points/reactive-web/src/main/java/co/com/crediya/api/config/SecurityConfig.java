package co.com.crediya.api.config;

import co.com.crediya.api.security.JwtService;
import co.com.crediya.model.solicitud.valueobjects.GeneralResponse;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JwtService jwtService) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(reg -> reg
                        .pathMatchers(
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/v3/api-docs", "/v3/api-docs/**", "/v3/api-docs/swagger-config",
                                "/api-docs", "/api-docs/**", "/api-docs/swagger-config",
                                "/swagger-resources", "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()
                        // Health + CORS preflight
                        .pathMatchers("/actuator/health", "/actuator/info").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(e -> e
                        .authenticationEntryPoint((exchange, ex) ->
                                writeJson(exchange, HttpStatus.UNAUTHORIZED, Map.of(
                                        "code", "AUTH_REQUIRED",
                                        "message", "Se requiere autenticación"
                                )))
                        .accessDeniedHandler((exchange, ex) ->
                                writeJson(exchange, HttpStatus.FORBIDDEN, Map.of(
                                        "code","INSUFFICIENT_ROLE",
                                        "message","No tienes permisos"
                                )))
                )
                .addFilterAt((exchange, chain) -> {
                    String path = exchange.getRequest().getPath().value();
                    if (path.startsWith("/swagger-ui")
                            || path.startsWith("/v3/api-docs")
                            || path.startsWith("/api-docs")
                            || path.startsWith("/swagger-resources")
                            || path.startsWith("/webjars")) {
                        return chain.filter(exchange);
                    }

                    return bearer(exchange)
                            .flatMap(token -> authenticate(token, jwtService))
                            .flatMap(auth -> chain.filter(exchange).contextWrite(
                                    ctx -> org.springframework.security.core.context.ReactiveSecurityContextHolder.withAuthentication(auth)
                            ))
                            .switchIfEmpty(chain.filter(exchange))
                            .onErrorResume(BadCredentialsException.class, e -> {
                                Throwable c = rootCause(e);
                                String code = "TOKEN_INVALID";
                                String msg  = "Token inválido";
                                if (c instanceof ExpiredJwtException)        { code = "TOKEN_EXPIRED";   msg = "El token ha expirado"; }
                                else if (c instanceof MalformedJwtException) { code = "TOKEN_MALFORMED"; msg = "El token está mal formado"; }
                                else if (c instanceof SignatureException
                                        || c instanceof io.jsonwebtoken.security.SecurityException) {
                                    code = "TOKEN_INVALID"; msg = "La firma del token no es válida";
                                }
                                return writeJson(exchange, HttpStatus.UNAUTHORIZED, Map.of("code", code, "message", msg));
                            });
                }, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private Mono<String> bearer(ServerWebExchange exchange) {
        var h = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        return (h != null && h.startsWith("Bearer ")) ? Mono.just(h.substring(7)) : Mono.empty();
    }

    private Mono<AbstractAuthenticationToken> authenticate(String token, JwtService jwtService) {
        try {
            var jws = jwtService.parseAndValidate(token);
            var claims = jws.getBody();

            String rol = claims.get("rol", String.class);
            var authorities = (rol == null)
                    ? List.<GrantedAuthority>of()
                    : List.of(new SimpleGrantedAuthority("ROLE_" + rol));

            var auth = new AbstractAuthenticationToken(authorities) {
                @Override public Object getCredentials() { return token; }
                @Override public Object getPrincipal()   { return claims.get("email"); }
            };
            auth.setAuthenticated(true);
            auth.setDetails(Map.copyOf(claims));
            return Mono.just(auth);
        } catch (Exception e) {
            return Mono.error(new BadCredentialsException("Invalid JWT", e));
        }
    }

    private static Throwable rootCause(Throwable t) {
        Throwable c = t;
        while (c.getCause() != null && c.getCause() != c) c = c.getCause();
        return c;
    }

    private Mono<Void> writeJson(ServerWebExchange exchange, HttpStatus status, Object error) {
        var res = exchange.getResponse();
        res.setStatusCode(status);
        res.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        var body = new GeneralResponse<>(status.value(), null, error);
        try {
            var bytes = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsBytes(body);
            return res.writeWith(Mono.just(res.bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}