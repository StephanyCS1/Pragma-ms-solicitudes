package co.com.crediya.model.solicitud;

import java.util.UUID;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LoanType {

    private UUID id;
    private String name;
    private String minAmount;
    private String maxAmount;
    private String interestRate;
    private String automaticValidation;

}
