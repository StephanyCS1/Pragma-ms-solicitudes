package co.com.crediya.exceptions;

import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.exceptions.RequestNotFoundException;
import co.com.crediya.model.solicitud.valueobjects.GeneralResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.codec.DecodingException;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Order(-2)
@Slf4j
public class GlobalExceptionHandler implements WebExceptionHandler {

    private static final String INTERNAL_SERVER_ERROR_MSG = "Error interno del servidor";
    private static final String INVALID_JSON_MSG = "JSON inválido o tipos incorrectos";

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        response.getHeaders().add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        GeneralResponse<Object> errorResponse;
        HttpStatus status;

        if (ex instanceof DomainValidationException domainEx) {
            status = HttpStatus.BAD_REQUEST;
            errorResponse = new GeneralResponse<>(
                    status.value(),
                    null,
                    domainEx.getErrors()
            );
            log.warn("Error de validación de dominio: {}", domainEx.getErrors());

        } else if (ex instanceof DecodingException) {
            status = HttpStatus.BAD_REQUEST;
            String msg = INVALID_JSON_MSG;
            Throwable cause = ex.getCause();
            if (cause instanceof ValueInstantiationException vie && vie.getCause() != null) {
                msg = vie.getCause().getMessage();
            }
            errorResponse = new GeneralResponse<>(
                    status.value(),
                    null,
                    msg
            );
            log.warn("Error de decodificación JSON: {}", ex.getMessage());

        } else if (ex instanceof ServerWebInputException inputEx) {
            status = HttpStatus.BAD_REQUEST;
            errorResponse = new GeneralResponse<>(
                    status.value(),
                    null,
                    inputEx.getReason()
            );
            log.warn("Error de entrada web: {}", inputEx.getReason());

        } else if (ex instanceof IllegalArgumentException && ex.getMessage().contains("UUID")) {
            status = HttpStatus.BAD_REQUEST;
            errorResponse = new GeneralResponse<>(
                    status.value(),
                    null,
                    "El id no es un UUID válido"
            );
            log.warn("UUID inválido: {}", ex.getMessage());

        }else if (ex instanceof RequestNotFoundException) {
            status = HttpStatus.NOT_FOUND;
            errorResponse = new GeneralResponse<>(
                    status.value(),
                    null,
                    ex.getMessage()
            );
            log.warn("Solicitud no encontrado: {}", ex.getMessage());
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
            errorResponse = new GeneralResponse<>(
                    status.value(),
                    null,
                    INTERNAL_SERVER_ERROR_MSG
            );
            log.error("Error inesperado", ex);
        }

        response.setStatusCode(status);

        try {
            ObjectMapper mapper = new ObjectMapper();
            String body = mapper.writeValueAsString(errorResponse);
            DataBuffer buffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
            return response.writeWith(Mono.just(buffer));
        } catch (JsonProcessingException e) {
            log.error("Error serializando respuesta de error", e);
            return Mono.error(e);
        }
    }
}