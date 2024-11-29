package islab1.models.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private long id;
    private long creatorId;
    private String zipCode;
}
