package co.com.crediya.r2dbc.entity;

import jakarta.persistence.GeneratedValue;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Table("request_entity")
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class RequestEntity {

    @Id
    @Column("id")
    @GeneratedValue
    private String id;

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

    @Column("created_at")
    private LocalDateTime created_at;

    @Column("updated_at")
    private LocalDateTime updated_at;

    @Column("user_id")
    private UUID userId;
}