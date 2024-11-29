package islab1.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import islab1.exceptions.ConvertionException;
import islab1.mappers.LocationMapper;
import islab1.models.DTO.LocationDTO;
import islab1.models.Location;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.LocationRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocationService {

    private final LocationMapper locationMapper;
    private final LocationRepo locationRepo;

    public List<Location> getAllLocations() {
        return locationRepo.findAll();
    }

    public List<Location> getLocationsByUser(User user) {
        return locationRepo.getLocationsByCreator(user);
    }

    public Location getLocationById(Long id) {
        return locationRepo.getById(id);
    }

    public boolean existById(Long id) {
        return locationRepo.existsById(id);
    }

    public Location createLocation(LocationDTO locationDTO) throws ConvertionException {
        Location location = locationMapper.toEntity(locationDTO);
        return locationRepo.save(location);
    }

    public Location updateLocation(Long id, LocationDTO locationDTO) throws ConvertionException, EntityNotFoundException {
        Location newLocation = locationMapper.toEntity(locationDTO);
        if (!existById(id)) {
            throw new EntityNotFoundException("There is no location with id " + id);
        }
        Location location = getLocationById(id);
        
        newLocation.setId(id);
        newLocation.setCreator(location.getCreator());
        locationRepo.save(newLocation);
        return newLocation;
    }

    public void deleteLocation(Long id){
        try{
            locationRepo.deleteById(id);
        }catch (Exception e){
            throw e;
        }
    }

    public boolean checkAccess(User user, Long locationId) {
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        Location location = getLocationById(locationId);
        return location != null && location.getCreator().equals(user);
    }

    public List<LocationDTO> convertLocationsToDTOs(List<Location> locations) {
        return locations.stream()
            .map(locationMapper::toDto)
            .collect(Collectors.toList());
    }

    public LocationDTO convertLocationToDTO(Location location) {
        return locationMapper.toDto(location);
    }
}
