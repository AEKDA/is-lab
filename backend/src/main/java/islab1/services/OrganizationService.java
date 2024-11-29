package islab1.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import islab1.exceptions.ConvertionException;
import islab1.mappers.OrganizationMapper;
import islab1.models.Organization;
import islab1.models.DTO.OrganizationDTO;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.OrganizationRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationMapper locationMapper;
    private final OrganizationRepo locationRepo;

    public List<Organization> getAllOrganizations() {
        return locationRepo.findAll();
    }

    public List<Organization> getOrganizationsByUser(User user) {
        return locationRepo.getOrganizationsByCreator(user);
    }

    public Organization getOrganizationById(Long id) {
        return locationRepo.getById(id);
    }

    public boolean existById(Long id) {
        return locationRepo.existsById(id);
    }

    public Organization createOrganization(OrganizationDTO locationDTO) throws ConvertionException {
        Organization location = locationMapper.toEntity(locationDTO);
        return locationRepo.save(location);
    }

    public Organization updateOrganization(Long id, OrganizationDTO locationDTO)
            throws ConvertionException, EntityNotFoundException {
        Organization newOrganization = locationMapper.toEntity(locationDTO);
        if (!existById(id)) {
            throw new EntityNotFoundException("There is no location with id " + id);
        }
        Organization location = getOrganizationById(id);

        newOrganization.setId(id);
        newOrganization.setCreator(location.getCreator());
        locationRepo.save(newOrganization);
        return newOrganization;
    }

    public void deleteOrganization(Long id) {
        try {
            locationRepo.deleteById(id);
        } catch (Exception e) {
            throw e;
        }
    }

    public boolean checkAccess(User user, Long locationId) {
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        Organization location = getOrganizationById(locationId);
        return location != null && location.getCreator().equals(user);
    }

    public List<OrganizationDTO> convertOrganizationsToDTOs(List<Organization> locations) {
        return locations.stream()
                .map(locationMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrganizationDTO convertOrganizationToDTO(Organization location) {
        return locationMapper.toDto(location);
    }
}
