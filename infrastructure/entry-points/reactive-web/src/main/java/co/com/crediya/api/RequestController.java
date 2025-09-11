package co.com.crediya.api;

import co.com.crediya.api.dto.NewRequest;
import co.com.crediya.api.dto.RequestDto;
import co.com.crediya.api.mapper.RequestMapper;
import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.exceptions.DomainValidationException;
import co.com.crediya.model.solicitud.valueobjects.GeneralResponse;
import co.com.crediya.model.solicitud.valueobjects.PagedResponse;
import co.com.crediya.model.solicitud.valueobjects.RequestSummary;
import co.com.crediya.model.solicitud.valueobjects.SortSpec;
import co.com.crediya.usecase.request.createrequest.CreateRequestUseCase;
import co.com.crediya.usecase.request.getallrequests.ListPendingRequestsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/solicitud")
@RequiredArgsConstructor
@Slf4j
public class RequestController {

    private final CreateRequestUseCase createRequestUseCase;
    private final ListPendingRequestsUseCase listPendingRequestsUseCase;
    private final RequestMapper requestMapper;

    @Tag(name = "Solicitudes de Crédito", description = "Gestión de solicitudes de crédito")
    @Operation(
            summary = "Registrar una nueva solicitud de crédito",
            description = """
                    Este endpoint permite a un cliente registrar una solicitud de crédito de manera asíncrona.
                    
                    **Proceso de validación:**
                    - ✅ Valida que el email exista en la base de datos de usuarios
                    - ✅ Valida que el tipo de crédito solicitado esté disponible
                    - ✅ Valida que el monto sea mayor a 0
                    - ✅ Valida que el plazo sea mayor o igual a 6 meses
                    - ✅ Registra la solicitud con estado inicial **PENDIENTE DE REVISIÓN**
                    
                    **Reglas de negocio:**
                    - El monto debe ser un valor positivo mayor a 0
                    - El plazo mínimo es de 6 meses
                    - El email debe corresponder a un usuario activo en el sistema
                    - Solo se permiten tipos de crédito habilitados en el sistema
                    
                    **Flujo del proceso:**
                    1. Recepción y validación inicial de la solicitud
                    2. Verificación de la existencia del usuario por email
                    3. Mapeo de la solicitud a comando interno
                    4. Creación de la solicitud en el sistema
                    5. Retorno de confirmación con ID de solicitud generada
                    """,
            operationId = "createCreditRequest",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = """
                                    **Solicitud creada exitosamente**
                                    
                                    La solicitud de crédito ha sido registrada correctamente en el sistema 
                                    y se encuentra en estado 'PENDIENTE DE REVISIÓN'. Se retorna la información 
                                    de la solicitud creada incluyendo el ID único generado.
                                    """,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(
                                            name = "Solicitud creada",
                                            summary = "Respuesta exitosa de creación",
                                            value = """
                                                    {
                                                      "code": 200,
                                                      "data": {
                                                        "id": "550e8400-e29b-41d4-a716-446655440000",
                                                        "documentNumber": "12345678901",
                                                        "email": "cliente@ejemplo.com",
                                                        "amount": 50000.00,
                                                        "term": 12,
                                                        "creditType": "PERSONAL",
                                                        "status": "PENDIENTE_DE_REVISION",
                                                        "createdAt": "2024-03-15T10:30:00Z"
                                                      },
                                                      "message": null
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = """
                                    **Error de validación en la solicitud**
                                    
                                    Los datos enviados no cumplen con las reglas de validación establecidas.
                                    Posibles causas:
                                    - Monto menor o igual a 0
                                    - Plazo menor a 6 meses
                                    - Tipo de crédito no válido
                                    - Formato de email incorrecto
                                    - Campos requeridos faltantes
                                    """,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(
                                            name = "Error de validación",
                                            summary = "Datos inválidos en la solicitud",
                                            value = """
                                                    {
                                                      "code": 400,
                                                      "data": null,
                                                      "message": "El monto debe ser mayor a 0 y el plazo mínimo es de 6 meses"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = """
                                    **Usuario no encontrado**
                                    
                                    El email proporcionado no corresponde a ningún usuario registrado 
                                    en el sistema. Verifique que:
                                    - El usuario esté registrado en la plataforma
                                    - La cuenta del usuario esté activa
                                    """,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(
                                            name = "Usuario no encontrado",
                                            summary = "Email no existe en la base de datos",
                                            value = """
                                                    {
                                                      "code": 404,
                                                      "data": null,
                                                      "message": "No se encontró un usuario con el email proporcionado"
                                                    }
                                                    """
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = """
                                    **Error interno del servidor**
                                    
                                    Ha ocurrido un error inesperado en el servidor al procesar la solicitud.
                                    Si el problema persiste, contacte al equipo de soporte técnico.
                                    """,
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class),
                                    examples = @ExampleObject(
                                            name = "Error interno",
                                            summary = "Error del servidor",
                                            value = """
                                                    {
                                                      "code": 500,
                                                      "data": null,
                                                      "message": "Error interno del servidor. Intente nuevamente más tarde"
                                                    }
                                                    """
                                    )
                            )
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','ASESOR','USER')")
    public Mono<ResponseEntity<GeneralResponse<Request>>> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Datos de la nueva solicitud de crédito",
                    required = true,
                    content = @Content(schema = @Schema(implementation = RequestDto.class))
            ) @RequestBody RequestDto request, Authentication authentication) {
        log.info("📩 Creando solicitud de crédito");
        @SuppressWarnings("unchecked")
        var claims = (Map<String, Object>) authentication.getDetails();
        String tokenEmail = (String) claims.get("email");
        String identification = (String) claims.get("identification");
        UUID userId = UUID.fromString((String) claims.get("uid"));
        String name = (String) claims.get("name");
        String lastname = (String) claims.get("lastName");

        log.info(userId.toString());
        log.info(tokenEmail);
        log.info(identification);
        log.info(name);
        log.info(lastname);
        var normalized = new NewRequest(
                identification,
                name,
                lastname,
                tokenEmail,
                request.phoneNumber(),
                request.requestedAmount(),
                request.loanTermMonths(),
                request.loanTypeId(),
                request.monthlyIncome(),
                userId
        );

        log.info(normalized.toString());
        return Mono.just(normalized)
                .doOnNext(req -> log.debug("📥 Payload procesado: {}", req))
                .doOnNext(req -> log.info("🚀 Procesando creación de solicitud para usuario: {} ({})",
                        tokenEmail, userId))
                .flatMap(normalizedRequest -> {
                    log.debug("🛠️ Mapeando solicitud a command para email: {}", normalizedRequest.email());
                    var command = requestMapper.toCommand(normalizedRequest);
                    log.debug("⚡ Ejecutando CreateRequestUseCase para usuario: {}", tokenEmail);
                    return createRequestUseCase.create(command);
                })
                .doOnNext(created -> log.info("✅ Solicitud creada exitosamente - ID: {} | Usuario: {} | Identificación: {}",
                        created.getId(), tokenEmail, created.getDocumentNumber()))
                .doOnError(error -> log.error("❌ Error creando solicitud para usuario {}: {}",
                        tokenEmail, error.getMessage()))
                .map(created -> ResponseEntity.ok(new GeneralResponse<>(200, created, null)));
    }


    @Operation(
            summary = "Listar solicitudes pendientes",
            description = "Devuelve una lista paginada de solicitudes con estado pendiente.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Lista de solicitudes pendientes",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Acceso denegado",
                            content = @Content
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyRole('ASESOR','ADMIN')")
    public Mono<ResponseEntity<GeneralResponse<PagedResponse<RequestSummary>>>> listPending(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size,
            @RequestParam(name = "direction", defaultValue = "DESC") String direction,
            @RequestParam(name = "sort", defaultValue = "created_at") String sortField,
            @RequestParam(name = "filterType", required = false) String filterType,
            @RequestParam(name = "filterValue", required = false) String filterValue,
            ServerHttpRequest request
    ) {
        var sort = new SortSpec(
                sortField,
                "DESC".equalsIgnoreCase(direction) ? SortSpec.Direction.DESC : SortSpec.Direction.ASC
        );
        String bearer = null;
        var auth = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            bearer = auth.substring(7);
        }
        if (bearer == null || bearer.isBlank()) {
            return Mono.error(new DomainValidationException("Falta token de autorización"));
        }        log.info(bearer);
        return listPendingRequestsUseCase.execute(page, size, filterType, filterValue, sort, bearer)
                .map(resp -> ResponseEntity.ok(new GeneralResponse<>(200, resp, null)));
    }
}
