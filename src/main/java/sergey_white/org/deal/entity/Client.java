package sergey_white.org.deal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sergey_white.org.deal.enums.Gender;
import sergey_white.org.deal.enums.MaritalStatus;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID client_Id;
    private String firstName;
    private String lastName;
    private String middleName;
    private LocalDate birthDate;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;
    private Integer dependentAmount;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passport_id")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonBackReference
    private Passport passport;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employment_id")
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonBackReference
    private Employment employment;
    @Column(unique = true)
    private String accountNumber;
    @OneToOne(mappedBy = "client",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Statement statement;
}