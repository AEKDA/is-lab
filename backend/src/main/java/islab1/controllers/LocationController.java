package islab1.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import islab1.auth.services.AuthenticationService;
import islab1.exceptions.ConvertionException;
import islab1.models.DTO.LocationDTO;
import islab1.models.Location;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.LocationService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/locations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class LocationController {

    private final LocationService locationService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<LocationDTO>> getAllLocations() {
        User user = authenticationService.getCurrentUser();
        List<Location> locations = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            locations = locationService.getAllLocations();
        } else {
            locations = locationService.getLocationsByUser(user);
        }
        List<LocationDTO> locationDTOs = locationService.convertLocationsToDTOs(locations);
        return ResponseEntity.ok(locationDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDTO> getLocationById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!locationService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Location not found").body(null);
        }
        if (!locationService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Location location = locationService.getLocationById(id);
        LocationDTO locationDTO = locationService.convertLocationToDTO(location);
        return ResponseEntity.ok(locationDTO);
    }

    @PostMapping
    public ResponseEntity<LocationDTO> createLocation(@RequestBody LocationDTO locationDTO) {
        User user = authenticationService.getCurrentUser();
        locationDTO.setCreatorId(user.getId());
        Location location;
        try {
            location = locationService.createLocation(locationDTO);
        } catch (ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        locationDTO = locationService.convertLocationToDTO(location);
        return ResponseEntity.ok(locationDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDTO> updateLocation(@PathVariable Long id, @RequestBody LocationDTO locationDTO) {
        User user = authenticationService.getCurrentUser();
        if (!locationService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Location not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !locationService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Location location;
        try {
            location = locationService.updateLocation(id, locationDTO);
        } catch (EntityNotFoundException | ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        locationDTO = locationService.convertLocationToDTO(location);
        return ResponseEntity.ok(locationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!locationService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Location not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !locationService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        try{
            locationService.deleteLocation(id);
        } catch (Exception e){
            return ResponseEntity.status(400).header("ErrMessage", "Unable to delete entity because it is used in other entity").body(null);
        }
        return ResponseEntity.ok().build();
    }
}
