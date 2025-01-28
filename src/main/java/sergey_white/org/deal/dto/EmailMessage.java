package sergey_white.org.deal.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import sergey_white.org.deal.enums.ApplicationStatus;

import java.util.UUID;


@Data
@Accessors(chain = true)
public class EmailMessage {
    private String address;
    private ApplicationStatus theme;
    private UUID statementId;
    private String text;
}
