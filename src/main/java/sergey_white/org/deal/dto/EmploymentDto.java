package sergey_white.org.deal.dto;

import lombok.Data;
import sergey_white.org.deal.enums.EmploymentPosition;
import sergey_white.org.deal.enums.EmploymentStatus;


import java.math.BigDecimal;

@Data
public class EmploymentDto {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
