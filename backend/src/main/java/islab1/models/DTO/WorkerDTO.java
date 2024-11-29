package islab1.models.DTO;

import java.time.LocalDateTime;

import islab1.models.Position;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkerDTO {
    private long id;
    private Long creatorId;
    private String name;
    private Long coordinatesId;
    private LocalDateTime  creationDate;
    private Long organizationId;
    private Long personId;
    private Long salary;
    private Double rating;
    private java.util.Date startDate;
    private LocalDateTime endDate;
    private Position position;
}
