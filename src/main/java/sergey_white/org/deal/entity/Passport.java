package sergey_white.org.deal.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;
    @OneToOne(mappedBy = "passport", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Client client;
}
