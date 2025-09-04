package co.com.crediya.api;

import co.com.crediya.api.dto.NewRequest;
import co.com.crediya.api.mapper.RequestMapper;
import co.com.crediya.model.solicitud.gateways.UserValidationService;
import co.com.crediya.model.solicitud.valueobjects.GeneralResponse;
import co.com.crediya.usecase.request.createrequest.CreateRequestUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/solicitud")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

  private final CreateRequestUseCase createRequestUseCase;
  private final RequestMapper requestMapper;
  private final UserValidationService userValidationService;

    @Operation(
            summary = "Registrar una nueva solicitud de crédito",
            description = """
                    Este endpoint permite a un cliente registrar una solicitud de crédito.
                    
                    - Valida que el email exista en la base de usuarios.  
                    - Valida que el tipo de crédito solicitado exista.  
                    - Registra la solicitud con estado inicial **PENDIENTE DE REVISIÓN**.  
                    
                    Reglas:  
                    - El monto debe ser mayor a 0.  
                    - El plazo debe ser mayor o igual a 6 meses.  
                    - El email debe existir en la BD de usuarios.  
                    """,
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Solicitud creada exitosamente",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error de validación en la solicitud",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Usuario no encontrado",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(schema = @Schema(implementation = GeneralResponse.class))
                    )
            }
    )
    @PostMapping
    public Mono<ResponseEntity<GeneralResponse<?>>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos de la nueva solicitud de crédito",
            required = true,
            content = @Content(schema = @Schema(implementation = NewRequest.class))
    )@RequestBody NewRequest request) {
        log.info("📩 Creando solicitud de crédito");

        return Mono.just(request)
                .doOnNext(req -> log.debug("Payload recibido: {}", req))
                .doOnNext(req -> log.info("🔎 Validando existencia de email: {}", req.email()))
                .flatMap(req -> userValidationService.findByEmail(req.email())
                        .map(user -> reactor.util.function.Tuples.of(req, user)))
                .doOnNext(tuple -> log.debug("✅ Usuario validado en BD: {}", tuple.getT2().email()))
                .flatMap(tuple -> {
                    var req = tuple.getT1();
                    var user = tuple.getT2();

                    log.debug("🛠️ Mapeando request a command para email: {}", req.email());
                    var command = requestMapper.toCommand(req);

                    log.debug("🚀 Ejecutando CreateRequestUseCase para usuario con email {}", user.email());
                    return createRequestUseCase.create(command);
                })
                .doOnNext(created -> log.info("✅ Solicitud generada con id={} identificación={}",
                        created.getId(), created.getDocumentNumber()))
                .map(created -> ResponseEntity.ok(
                        new GeneralResponse<>(200, created, null)
                ));
    }
}
