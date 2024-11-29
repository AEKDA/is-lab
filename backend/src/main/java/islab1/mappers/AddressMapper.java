package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.Address;
import islab1.models.DTO.AddressDTO;
import islab1.models.auth.User;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AddressMapper {
    private final UserRepo userRepo;

    public Address toEntity(AddressDTO dto) throws ConvertionException {
        try {
            User creator = userRepo.getReferenceById(dto.getCreatorId());
            Address address = new Address();
            address.setCreator(creator);
            address.setZipCode(dto.getZipCode());
            return address;
        } catch (ConvertionException e) {
            throw e;
        } catch (EntityNotFoundException e) {
            throw new ConvertionException(e.getMessage());
        }
    }

    public AddressDTO toDto(Address address) {  
        AddressDTO dto = new AddressDTO();
        dto.setId(address.getId());
        dto.setCreatorId(address.getCreator().getId());
        dto.setZipCode(address.getZipCode());
        return dto;
    }
}
