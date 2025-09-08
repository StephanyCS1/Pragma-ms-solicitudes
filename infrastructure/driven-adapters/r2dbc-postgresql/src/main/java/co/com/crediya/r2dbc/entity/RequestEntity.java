package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("requests")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class RequestEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("document")
    private String document;

    @Column("email")
    private String email;

    @Column("requested_amount")
    private BigDecimal requestedAmount;

    @Column("loan_term")
    private Integer loanTerm;

    @Column("loan_type_id")
    private UUID loanTypeId;

    @Column("status_id")
    private UUID statusId;

    @Column("request_date")
    private LocalDateTime requestDate;

    @Column("last_update_date")
    private LocalDateTime lastUpdateDate;

    @Column("user_id")
    private UUID userId;
}