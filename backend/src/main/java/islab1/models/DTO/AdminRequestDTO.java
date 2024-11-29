package islab1.models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminRequestDTO {
    private Long id;
    private String username;
    private String password;
    private Long reviewerId;
}
