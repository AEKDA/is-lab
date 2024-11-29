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
import islab1.models.Organization;
import islab1.models.DTO.OrganizationDTO;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.OrganizationService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/organizations")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrganizationsController {

    private final OrganizationService organizationsService;
    private final AuthenticationService authenticationService;

    @GetMapping
    public ResponseEntity<List<OrganizationDTO>> getAllOrganizations() {
        User user = authenticationService.getCurrentUser();
        List<Organization> organizations = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            organizations = organizationsService.getAllOrganizations();
        } else {
            organizations = organizationsService.getOrganizationsByUser(user);
        }
        List<OrganizationDTO> coordinatesDTOs = organizationsService.convertOrganizationsToDTOs(organizations);
        return ResponseEntity.ok(coordinatesDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationDTO> getOrganizationById(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!organizationsService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Organizations not found").body(null);
        }
        if (!organizationsService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Organization organizations = organizationsService.getOrganizationById(id);
        OrganizationDTO organizationDTO = organizationsService.convertOrganizationToDTO(organizations);
        return ResponseEntity.ok(organizationDTO);
    }

    @PostMapping
    public ResponseEntity<OrganizationDTO> createOrganization(@RequestBody OrganizationDTO organizationDTO) {
        User user = authenticationService.getCurrentUser();
        organizationDTO.setCreatorId(user.getId());
        Organization organizations;
        try {
            organizations = organizationsService.createOrganization(organizationDTO);
        } catch (ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        organizationDTO = organizationsService.convertOrganizationToDTO(organizations);
        return ResponseEntity.ok(organizationDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationDTO> updateOrganizations(@PathVariable Long id, @RequestBody OrganizationDTO organizationDTO) {
        User user = authenticationService.getCurrentUser();
        if (!organizationsService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Organizations not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !organizationsService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        Organization organizations;
        try {
            organizations = organizationsService.updateOrganization(id, organizationDTO);
        } catch (EntityNotFoundException | ConvertionException e) {
            return ResponseEntity.status(400).header("ErrMessage", e.getMessage()).body(null);
        }
        organizationDTO = organizationsService.convertOrganizationToDTO(organizations);
        return ResponseEntity.ok(organizationDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganizations(@PathVariable Long id) {
        User user = authenticationService.getCurrentUser();
        if (!organizationsService.existById(id)) {
            return ResponseEntity.status(400).header("ErrMessage", "Organizations not found").body(null);
        }
        if (user.getRole() != Role.ADMIN && !organizationsService.checkAccess(user, id)) {
            return ResponseEntity.status(403).header("ErrMessage", "Access denied").body(null);
        }
        try {
            organizationsService.deleteOrganization(id);
        } catch (Exception e) {
            return ResponseEntity.status(400).header("ErrMessage", "Unable to delete entity because it is used in other entity").body(null);
        }
        return ResponseEntity.ok().build();
    }
}
