package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table(name = "requestEntity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RequestEntity {

    @Id
    private UUID id;

    @Column("document")
    private String documentNumber;

    @Column("email")
    private String email;

    @Column("requestedAmount")
    private BigDecimal requestedAmount;

    @Column("loanTerm")
    private String loanTerm;

    @Column("loanTypeId")
    private UUID loanTypeId;

    @Column("statusId")
    private UUID statusId;

    @Column("requestDate")
    private LocalDateTime requestDate;

    @Column("lastUpdateDate")
    private LocalDateTime lastUpdateDate;

}
