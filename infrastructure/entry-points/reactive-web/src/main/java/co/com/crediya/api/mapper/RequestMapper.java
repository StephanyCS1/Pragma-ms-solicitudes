package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.NewRequest;
import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.valueobjects.Amount;
import co.com.crediya.model.solicitud.valueobjects.CreateRequestCommand;
import co.com.crediya.model.solicitud.valueobjects.Email;
import co.com.crediya.model.solicitud.valueobjects.Identification;
import co.com.crediya.model.solicitud.valueobjects.LoanTerm;
import co.com.crediya.model.solicitud.valueobjects.Name;
import co.com.crediya.model.solicitud.valueobjects.UserId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {
                BigDecimal.class,
                LocalDateTime.class,
                UUID.class,
                Amount.class,
                Email.class,
                Identification.class,
                LoanTerm.class,
                Name.class,
                UserId.class
        }
)
public interface RequestMapper {

    @Mapping(target = "documentNumber", source = "documentNumber")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "requestedAmount", expression = "java(dto.requestedAmount().toString())")
    @Mapping(target = "loanTermMonths", source = "loanTermMonths")
    @Mapping(target = "loanTypeId", source = "loanTypeId")
    @Mapping(target = "monthlyIncome", constant = "6")
    @Mapping(target = "userId")
    CreateRequestCommand toCommand(NewRequest dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name",            expression = "java(new Name(command.firstName(), command.lastName()))")
    @Mapping(target = "documentNumber",  expression = "java(new Identification(command.documentNumber()))")
    @Mapping(target = "email",           expression = "java(new Email(command.email()))")
    @Mapping(target = "requestedAmount", expression = "java(new Amount(new BigDecimal(command.requestedAmount())))")
    @Mapping(target = "loanTerm",        expression = "java(new LoanTerm(command.loanTermMonths()))")
    @Mapping(target = "loanTypeId", source = "loanTypeId")
    @Mapping(target = "statusId",        expression = "java(UUID.fromString(\"4f646f64-e460-43d1-9137-0201d4eb3743\"))")
    @Mapping(target = "requestDate",     expression = "java(LocalDateTime.now())")
    @Mapping(target = "lastUpdateDate",  expression = "java(LocalDateTime.now())")
    @Mapping(target = "userId",          expression = "java(new UserId(command.userId()))")
    Request toDomain(CreateRequestCommand command);
}