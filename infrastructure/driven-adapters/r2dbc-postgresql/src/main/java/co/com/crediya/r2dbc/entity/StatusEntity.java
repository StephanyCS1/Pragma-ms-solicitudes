package co.com.crediya.r2dbc.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "status")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class StatusEntity {

    @Id
    private UUID id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;

}
