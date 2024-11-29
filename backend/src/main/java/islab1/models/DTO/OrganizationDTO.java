package islab1.models.DTO;

import islab1.models.OrganizationType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizationDTO {
    private Long id;
    private String fullName;
    private Integer annualTurnover;
    private Long employeesCount;
    private OrganizationType type;
    private Long addressId;
    private long creatorId;
}
