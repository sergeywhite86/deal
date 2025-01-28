package sergey_white.org.deal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import sergey_white.org.deal.enums.EmploymentPosition;
import sergey_white.org.deal.enums.EmploymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
public class Employment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;
    private String employerInn;
    private BigDecimal salary;
    @Enumerated(EnumType.STRING)
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
    @OneToOne(mappedBy = "employment", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Client client;
}
