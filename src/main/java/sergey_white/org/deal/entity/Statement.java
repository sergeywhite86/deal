package sergey_white.org.deal.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import sergey_white.org.deal.dto.LoanOfferDto;
import sergey_white.org.deal.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Statement {
    @Id
    private UUID statementId;
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
    @CreationTimestamp
    private LocalDateTime creationDate;
    @JdbcTypeCode(SqlTypes.JSON)
    private LoanOfferDto appliedOffer;
    private LocalDateTime signDate;
    private String sesCode;
    @JdbcTypeCode(SqlTypes.JSON)
    private List<StatusHistory> statusHistory;
    @OneToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;
    @OneToOne
    @JoinColumn(name = "credit_id")
    @JsonManagedReference
    private Credit credit;
}
