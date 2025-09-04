package co.com.crediya.api.mapper;

import co.com.crediya.api.dto.NewRequest;
import co.com.crediya.model.solicitud.Request;
import co.com.crediya.model.solicitud.valueobjects.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Mapper(
        componentModel = "spring",
        imports = {
                UUID.class,
                LocalDateTime.class,
                BigDecimal.class,
                Identification.class,
                Email.class,
                Amount.class,
                LoanTerm.class
        }
)
public interface RequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "documentNumber", expression = "java(new Identification(command.documentNumber()))")
    @Mapping(target = "email", expression = "java(new Email(command.email()))")
    @Mapping(target = "requestedAmount", expression = "java(new Amount(new BigDecimal(command.requestedAmount())))")
    @Mapping(target = "loanTerm", expression = "java(new LoanTerm(command.loanTermMonths()))")
    @Mapping(target = "loanTypeId", source = "loanTypeId")
    @Mapping(target = "statusId", constant = "1L")
    @Mapping(target = "requestDate", expression = "java(LocalDateTime.now())")
    @Mapping(target = "lastUpdateDate", expression = "java(LocalDateTime.now())")
    Request toDomain(CreateRequestCommand command);

    @Mapping(target = "documentNumber", source = "documentNumber")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "requestedAmount", expression = "java(dto.requestedAmount().toString())")
    @Mapping(target = "loanTermMonths", source = "loanTermMonths")
    @Mapping(target = "loanTypeId", source = "loanTypeId")
    @Mapping(target = "monthlyIncome", constant = "0")
    CreateRequestCommand toCommand(NewRequest dto);
}