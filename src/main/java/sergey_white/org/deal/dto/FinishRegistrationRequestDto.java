package sergey_white.org.deal.dto;

import lombok.Data;
import sergey_white.org.deal.enums.Gender;
import sergey_white.org.deal.enums.MaritalStatus;

import java.time.LocalDate;

@Data
public class FinishRegistrationRequestDto {
    private Gender gender;
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDto employment;
    private String accountNumber;
}
