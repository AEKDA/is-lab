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
import islab1.models.Worker;
import islab1.models.DTO.WorkerDTO;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.CoordinatesService;
import islab1.services.PersonService;
import islab1.services.WorkerService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/workers")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class WorkerController {

    private final WorkerService workerService;
    private final AuthenticationService authenticationService;
    private final CoordinatesService coordinatesService;
    private final PersonService personService;

    @GetMapping
    public ResponseEntity<List<WorkerDTO>> getAllWorkers() {
        User user = authenticationService.getCurrentUser();
        List<Worker> workers = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            workers = workerService.getAllWorkers();
        } else {
            workers = workerService.getWorkersByUser(user);
        }
        List<WorkerDTO> workerDTOs = workerService.convertWorkersToDTOs(workers);
        return ResponseEntity.ok(workerDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkerDTO> getWorkerById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!workerService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Worker with that id does not exist").body(null);
        }
        if (!workerService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Worker worker = workerService.getWorkerById(id);
        WorkerDTO workerDTO = workerService.convertWorkerToDTO(worker);
        return ResponseEntity.ok(workerDTO);
    }

    @PostMapping
    public ResponseEntity<WorkerDTO> createWorker(@RequestBody WorkerDTO workerDTO) {
        User user = authenticationService.getCurrentUser();
        ResponseEntity<WorkerDTO> validationResults = validateLinkedObjects(user, workerDTO);
        if (validationResults != null) {
            return validationResults;
        }
        workerDTO.setCreatorId(user.getId());
        Worker worker;
        try {
            worker = workerService.createWorker(workerDTO);
        } catch (Exception e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        workerDTO = workerService.convertWorkerToDTO(worker);
        return ResponseEntity.ok(workerDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkerDTO> updateWorker(@PathVariable Long id, @RequestBody WorkerDTO workerDTO) {
        User user = authenticationService.getCurrentUser();
        ResponseEntity<WorkerDTO> validationResults = validateLinkedObjects(user, workerDTO);
        if (validationResults != null) {
            return validationResults;
        }
        if (!workerService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Worker with that id does not exist").body(null);
        }
        if (user.getRole() != Role.ADMIN && !workerService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Worker worker;
        try {
            worker = workerService.updateWorker(id, workerDTO);
        } catch (EntityNotFoundException | ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        workerDTO = workerService.convertWorkerToDTO(worker);
        return ResponseEntity.ok(workerDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWorker(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!workerService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Worker with that id does not exist").body(null);
        }
        if (user.getRole() != Role.ADMIN && !workerService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        try {
            workerService.deleteWorker(id);
        } catch (Exception e) {
            return ResponseEntity.status(400)
                    .header("ErrMessage", "Unable to delete worker, it is linked to other entities").body(null);
        }
        return ResponseEntity.ok().build();
    }

    private ResponseEntity<WorkerDTO> validateLinkedObjects(User user, WorkerDTO workerDTO) {
        if (!coordinatesService.existById(workerDTO.getCoordinatesId())) {
            return ResponseEntity.status(400).header("ErrMessage", "Coordinates with that id do not exist").body(null);
        }
        if (coordinatesService.getCoordinatesById(workerDTO.getCoordinatesId()).getCreator() != user
                && user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(400)
                    .header("ErrMessage", "Cannot link object with coordinates not created by you").body(null);
        }

        if (!personService.existById(workerDTO.getPersonId())) {
            return ResponseEntity.status(400).header("ErrMessage", "Person with that id does not exist").body(null);
        }
        if (personService.getPersonById(workerDTO.getPersonId()).getCreator() != user && user.getRole() != Role.ADMIN) {
            return ResponseEntity.status(400).header("ErrMessage", "Cannot link object with person not created by you")
                    .body(null);
        }

        return null;
    }
}
