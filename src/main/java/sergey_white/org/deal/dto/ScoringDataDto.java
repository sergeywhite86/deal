package sergey_white.org.deal.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import sergey_white.org.deal.enums.Gender;
import sergey_white.org.deal.enums.MaritalStatus;


import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ScoringDataDto {
    @NotNull
    @DecimalMin(value = "100000")
    private BigDecimal amount;
    @NotNull
    @Min(value = 6)
    private Integer term;
    @NotBlank
    @Size(min = 2, max = 30)
    private String firstName;
    @NotBlank
    @Size(min = 2, max = 30)
    private String lastName;
    @Size(min = 2, max = 30)
    private String middleName;
    private Gender gender;
    @NotNull
    @PastOrPresent
    private LocalDate birthdate;
    @NotBlank
    @Pattern(regexp = "^\\d{4}$")
    private String passportSeries;
    @NotBlank
    @Pattern(regexp = "^\\d{6}$")
    private String passportNumber;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private EmploymentDto employment;
    private String accountNumber;
    private Boolean isInsuranceEnabled;
    private Boolean isSalaryClient;
}