package islab1.models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {
    private long id;
    private long creatorId;
    private double x;
    private Float y;
    private String name;
}
