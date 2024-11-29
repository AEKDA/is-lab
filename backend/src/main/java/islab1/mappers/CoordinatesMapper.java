package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.Coordinates;
import islab1.models.DTO.CoordinatesDTO;
import islab1.models.auth.User;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class CoordinatesMapper {

    private final UserRepo userRepo;

    public Coordinates toEntity(CoordinatesDTO dto) throws ConvertionException {
        try {
            User creator = userRepo.getReferenceById(dto.getCreatorId());
            Coordinates coordinates = new Coordinates();
            coordinates.setCreator(creator);
            coordinates.setX(dto.getX());
            coordinates.setY(dto.getY());
            return coordinates;
        }catch (ConvertionException e) {
            throw e;
        }
        catch (EntityNotFoundException e) {
            throw new ConvertionException(e.getMessage());
        }
    }

    public CoordinatesDTO toDto(Coordinates coordinates) {
        CoordinatesDTO dto = new CoordinatesDTO();
        dto.setId(coordinates.getId());
        dto.setCreatorId(coordinates.getCreator().getId());
        dto.setX(coordinates.getX());
        dto.setY(coordinates.getY());
        return dto;
    }
}
