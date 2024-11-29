package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.Address;
import islab1.models.Organization;
import islab1.models.DTO.OrganizationDTO;
import islab1.models.auth.User;
import islab1.repos.AddressRepo;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OrganizationMapper {

    private final UserRepo userRepo;
    private final AddressRepo addressRepo;

    public Organization toEntity(OrganizationDTO dto) throws ConvertionException {
        try {
            User creator = userRepo.getReferenceById(dto.getCreatorId());
            Address address = addressRepo.getReferenceById(dto.getAddressId());
            Organization organization = new Organization();
            organization.setCreator(creator);
            organization.setAnnualTurnover(dto.getAnnualTurnover());
            organization.setEmployeesCount(dto.getEmployeesCount());
            organization.setOfficialAddress(address);
            organization.setType(dto.getType());
            organization.setFullName(dto.getFullName());
            return organization;
        } catch (ConvertionException e) {
            throw e;
        } catch (EntityNotFoundException e) {
            throw new ConvertionException(e.getMessage());
        }
    }

    public OrganizationDTO toDto(Organization organization) {
        OrganizationDTO dto = new OrganizationDTO();
        dto.setId(organization.getId());
        dto.setCreatorId(organization.getCreator().getId());
        dto.setFullName(organization.getFullName());
        dto.setAddressId(organization.getOfficialAddress().getId());
        dto.setEmployeesCount(organization.getEmployeesCount());
        dto.setAnnualTurnover(organization.getAnnualTurnover());
        dto.setType(organization.getType());
        return dto;
    }
}
