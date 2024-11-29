package islab1.models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoordinatesDTO {
    private Long id;
    private long creatorId;
    private int x;
    private Integer y;
}