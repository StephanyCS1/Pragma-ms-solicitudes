package co.com.crediya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "loanTypeEntity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanTypeEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("minimumAmount")
    private String minimumAmount;

    @Column("maximumAmount")
    private String maximumAmount;

    @Column("interestRate")
    private String interestRate;

    @Column("autoValidation")
    private boolean autoValidation;

}
