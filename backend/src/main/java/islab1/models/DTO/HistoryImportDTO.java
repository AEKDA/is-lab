package islab1.models.DTO;

import java.time.LocalDateTime;

import islab1.models.StatusImport;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HistoryImportDTO {
    private long id;
    private LocalDateTime createdAt;
    private StatusImport status;
    private int count;
    private long creatorId;
    private String fileLink;
}
