package islab1.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import islab1.exceptions.ConvertionException;
import islab1.mappers.CoordinatesMapper;
import islab1.models.Coordinates;
import islab1.models.DTO.CoordinatesDTO;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.CoordinatesRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoordinatesService {

    private final CoordinatesMapper coordinatesMapper;
    private final CoordinatesRepo coordinatesRepo;

    public List<Coordinates> getAllCoordinates() {
        return coordinatesRepo.findAll();
    }

    public List<Coordinates> getCoordinatesByUser(User user) {
        return coordinatesRepo.getCoordinatesByCreator(user);
    }

    public Coordinates getCoordinatesById(Long id) {
        return coordinatesRepo.getById(id);
    }

    public boolean existById(Long id){
        return coordinatesRepo.existsById(id);
    }

    public Coordinates createCoordinates(CoordinatesDTO coordinatesDTO) throws ConvertionException {
        Coordinates coordinates = coordinatesMapper.toEntity(coordinatesDTO);
        return coordinatesRepo.save(coordinates);
    }

    public Coordinates updateCoordinates(Long id, CoordinatesDTO coordinatesDTO) throws ConvertionException, EntityNotFoundException {
        Coordinates newCoordinates = coordinatesMapper.toEntity(coordinatesDTO);
        if(!existById(id)){
            throw new EntityNotFoundException("There is no coordinates with id " + id);
        }
        Coordinates coordinates = getCoordinatesById(id);
        
        newCoordinates.setId(id);
        newCoordinates.setCreator(coordinates.getCreator());
        coordinatesRepo.save(newCoordinates);
        return newCoordinates;
    }

    public void deleteCoordinates(Long id) {
        try{
            coordinatesRepo.deleteById(id);
        }catch (Exception e){
            throw e;
        }
    }

    public boolean checkAccess(User user, Long coordinatesId){
        if(user.getRole().equals(Role.ADMIN)){
            return true;
        }
        Coordinates coordinates = getCoordinatesById(coordinatesId);
        return coordinates != null && coordinates.getCreator() == user;
    }

    public List<CoordinatesDTO> convertCoordinatesToDTOs(List<Coordinates> coordinates) {
            return coordinates.stream()
            .map(coordinatesMapper::toDto)
            .collect(Collectors.toList());
    }

    public CoordinatesDTO convertCoordinatesToDTOs(Coordinates coordinates) {
        return coordinatesMapper.toDto(coordinates);
}
}
