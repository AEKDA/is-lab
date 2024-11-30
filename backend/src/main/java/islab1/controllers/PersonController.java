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
import islab1.models.DTO.PersonDTO;
import islab1.models.Person;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.LocationService;
import islab1.services.PersonService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/persons")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PersonController {

    private final PersonService personService;
    private final AuthenticationService authenticationService;
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<List<PersonDTO>> getAllPersons() {
        User user = authenticationService.getCurrentUser();
        List<Person> persons = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            persons = personService.getAllPersons();
        } else {
            persons = personService.getPersonsByUser(user);
        }
        List<PersonDTO> personDTOs = personService.convertPersonsToDTOs(persons);
        return ResponseEntity.ok(personDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDTO> getPersonById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!personService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Person not found").body(null);
        }
        if (!personService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Person person = personService.getPersonById(id);
        PersonDTO personDTO = personService.convertPersonToDTO(person);
        return ResponseEntity.ok(personDTO);
    }

    @PostMapping
    public ResponseEntity<PersonDTO> createPerson(@RequestBody PersonDTO personDTO) {
        User user = authenticationService.getCurrentUser();
        if (!locationService.existById(personDTO.getLocationId())) {
            return ResponseEntity.status(400).header("ErrMessage", "Location with that id does not exist").body(null);
        }
        if (locationService.getLocationById(personDTO.getLocationId()).getCreator() != user) {
            return ResponseEntity.status(400).header("ErrMessage", "You cannot create an object linked to a location you don't own").body(null);
        }
        personDTO.setCreatorId(user.getId());
        Person person;
        try {
            person = personService.createPerson(personDTO);
        } catch (ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        personDTO = personService.convertPersonToDTO(person);
        return ResponseEntity.ok(personDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonDTO> updatePerson(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        User user = authenticationService.getCurrentUser();
        if (!personService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Person not found").body(null);
        }
        if (!locationService.existById(personDTO.getLocationId())) {
            return ResponseEntity.status(400).header("ErrMessage", "Location with that id does not exist").body(null);
        }
        // if (locationService.getLocationById(personDTO.getLocationId()).getCreator() != user) {
        //     return ResponseEntity.status(400).header("ErrMessage", "You cannot update an object linked to a location you don't own").body(null);
        // }
        if (user.getRole() != Role.ADMIN && !personService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Person person;
        try {
            person = personService.updatePerson(id, personDTO);
        } catch (EntityNotFoundException | ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        personDTO = personService.convertPersonToDTO(person);
        return ResponseEntity.ok(personDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!personService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Person not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !personService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        try {
            personService.deletePerson(id);
        } catch (Exception e) {
            return ResponseEntity.status(400).header("ErrMessage", "Unable to delete entity because it is used in another entity").body(null);
        }
        return ResponseEntity.ok().build();
    }
}
