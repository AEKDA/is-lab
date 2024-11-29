package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.DTO.LocationDTO;
import islab1.models.Location;
import islab1.models.auth.User;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class LocationMapper {

    private final UserRepo userRepo;

    public Location toEntity(LocationDTO dto) throws ConvertionException {
        try {
            User creator = userRepo.getReferenceById(dto.getCreatorId());
            Location location = new Location();
            location.setCreator(creator);
            location.setX(dto.getX());
            location.setY(dto.getY());
            location.setName(dto.getName());
            return location;
        } catch (ConvertionException e) {
            throw e;
        } catch (EntityNotFoundException e) {
            throw new ConvertionException(e.getMessage());
        }
    }

    public LocationDTO toDto(Location location) {
        LocationDTO dto = new LocationDTO();
        dto.setId(location.getId());
        dto.setCreatorId(location.getCreator().getId());
        dto.setX(location.getX());
        dto.setY(location.getY());
        dto.setName(location.getName());
        return dto;
    }
}
