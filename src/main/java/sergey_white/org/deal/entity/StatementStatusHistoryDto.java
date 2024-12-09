package sergey_white.org.deal.entity;

import lombok.Data;
import sergey_white.org.deal.enums.ChangeType;

import java.time.LocalDateTime;

@Data
public class StatementStatusHistoryDto {
    private StatusHistory status;
    private LocalDateTime time;
    private ChangeType changeType;
}
