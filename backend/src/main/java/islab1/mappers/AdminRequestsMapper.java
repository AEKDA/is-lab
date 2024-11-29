package islab1.mappers;

import org.springframework.stereotype.Component;

import islab1.models.DTO.AdminRequestDTO;
import islab1.models.auth.AdminRequest;

@Component
public class AdminRequestsMapper {
    
    public AdminRequestDTO toDto(AdminRequest adminRequest) {
        AdminRequestDTO dto = new AdminRequestDTO();
        dto.setId(adminRequest.getId());
        dto.setUsername(adminRequest.getUsername());
        if(adminRequest.getReviewer() != null){
            dto.setReviewerId(adminRequest.getReviewer().getId());
        }
        return dto;
    }

}
