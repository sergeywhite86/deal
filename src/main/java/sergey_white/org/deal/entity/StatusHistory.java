package sergey_white.org.deal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import sergey_white.org.deal.enums.ApplicationStatus;
import sergey_white.org.deal.enums.ChangeType;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatusHistory {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
