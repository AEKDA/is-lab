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
import islab1.models.Coordinates;
import islab1.models.DTO.CoordinatesDTO;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.CoordinatesService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/coordinates")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CoordinatesController {

    private final CoordinatesService coordinatesService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<CoordinatesDTO>> getAllCoordinates() {
        User user = authenticationService.getCurrentUser();
        List<Coordinates> coordinates = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            coordinates = coordinatesService.getAllCoordinates();
        } else {
            coordinates = coordinatesService.getCoordinatesByUser(user);
        }
        List<CoordinatesDTO> coordinatesDTOs = coordinatesService.convertCoordinatesToDTOs(coordinates);
        return ResponseEntity.ok(coordinatesDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CoordinatesDTO> getCoordinatesById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!coordinatesService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Coordinates not found").body(null);
        }
        if (!coordinatesService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Coordinates coordinates = coordinatesService.getCoordinatesById(id);
        CoordinatesDTO coordinatesDTO = coordinatesService.convertCoordinatesToDTOs(coordinates);
        return ResponseEntity.ok(coordinatesDTO);
    }

    @PostMapping
    public ResponseEntity<CoordinatesDTO> createCoordinates(@RequestBody CoordinatesDTO coordinatesDTO) {
        User user = authenticationService.getCurrentUser();
        coordinatesDTO.setCreatorId(user.getId());
        Coordinates coordinates;
        try {
            coordinates = coordinatesService.createCoordinates(coordinatesDTO);
        } catch (ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        coordinatesDTO = coordinatesService.convertCoordinatesToDTOs(coordinates);
        return ResponseEntity.ok(coordinatesDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CoordinatesDTO> updateCoordinates(@PathVariable Long id, @RequestBody CoordinatesDTO coordinatesDTO) {
        User user = authenticationService.getCurrentUser();
        if (!coordinatesService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Coordinates not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !coordinatesService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Coordinates coordinates;
        try {
            coordinates = coordinatesService.updateCoordinates(id, coordinatesDTO);
        } catch (EntityNotFoundException | ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        coordinatesDTO = coordinatesService.convertCoordinatesToDTOs(coordinates);
        return ResponseEntity.ok(coordinatesDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoordinates(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!coordinatesService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Coordinates not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !coordinatesService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        try {
            coordinatesService.deleteCoordinates(id);
        } catch (Exception e) {
            return ResponseEntity.status(400).header("ErrMessage", "Unable to delete entity because it is used in other entity").body(null);
        }
        return ResponseEntity.ok().build();
    }
}
