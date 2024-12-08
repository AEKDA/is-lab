package islab1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import islab1.mappers.AddressMapper;
import islab1.mappers.CoordinatesMapper;
import islab1.mappers.LocationMapper;
import islab1.mappers.OrganizationMapper;
import islab1.mappers.PersonMapper;
import islab1.mappers.WorkerMapper;
import islab1.models.Address;
import islab1.models.Coordinates;
import islab1.models.HistoryImport;
import islab1.models.Location;
import islab1.models.Organization;
import islab1.models.Person;
import islab1.models.StatusImport;
import islab1.models.Worker;
import islab1.models.DTO.AddressDTO;
import islab1.models.DTO.CoordinatesDTO;
import islab1.models.DTO.LocationDTO;
import islab1.models.DTO.OrganizationDTO;
import islab1.models.DTO.PersonDTO;
import islab1.models.DTO.WorkerDTO;
import islab1.models.auth.User;
import islab1.repos.HistoryImportRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploaderService {

    @Autowired
    private final WorkerService workerService;
    @Autowired
    private final CoordinatesService coordinatesService;
    @Autowired
    private final LocationService locationService;
    @Autowired
    private final PersonService personService;
    @Autowired
    private final AddressService addressService;
    @Autowired
    private final OrganizationService organizationService;
    @Autowired
    private final CoordinatesMapper coordinatesMapper;
    @Autowired
    private final LocationMapper locationMapper;
    @Autowired
    private final PersonMapper personMapper;
    @Autowired
    private final OrganizationMapper organizationMapper;
    @Autowired
    private final WorkerMapper workerMapper;
    @Autowired
    private final AddressMapper addressMapper;
    @Autowired
    private final HistoryImportRepo historyImportRepo;

    public void importData(MultipartFile file, User owner) throws Exception {
        HistoryImport historyImport = new HistoryImport();
        historyImport.setCreator(owner);
        try {
            historyImport.setCount(importEntities(file, owner));
            historyImport.setStatus(StatusImport.SUCCESS);
        } catch (Exception e) {
            historyImport.setStatus(StatusImport.FAIL);
        }
        historyImportRepo.save(historyImport);
    }

    @Transactional
    private int importEntities(MultipartFile file, User owner) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper = objectMapper.findAndRegisterModules();
        List<Worker> entities = objectMapper.readValue(file.getInputStream(),
                new TypeReference<List<Worker>>() {
                });
        for (Worker entity : entities) {
            entity.getCoordinates().setCreator(owner);
            CoordinatesDTO coordDto = coordinatesMapper.toDto(entity.getCoordinates());
            Coordinates newCoord = coordinatesService.createCoordinates(coordDto);

            entity.getPerson().getLocation().setCreator(owner);
            LocationDTO locDto = locationMapper.toDto(entity.getPerson().getLocation());
            Location newLoc = locationService.createLocation(locDto);

            entity.getPerson().setCreator(owner);
            PersonDTO persDto = personMapper.toDto(entity.getPerson());
            persDto.setLocationId(newLoc.getId());
            Person newPers = personService.createPerson(persDto);

            entity.getOrganization().getOfficialAddress().setCreator(owner);
            AddressDTO addressDto = addressMapper.toDto(entity.getOrganization().getOfficialAddress());
            Address newAddress = addressService.createAddress(addressDto);

            entity.getOrganization().setCreator(owner);
            OrganizationDTO orgDto = organizationMapper.toDto(entity.getOrganization());
            orgDto.setAddressId(newAddress.getId());
            Organization newOrg = organizationService.createOrganization(orgDto);

            entity.setCreator(owner);
            WorkerDTO workerDTO = workerMapper.toDto(entity);
            workerDTO.setCoordinatesId(newCoord.getId());
            workerDTO.setPersonId(newPers.getId());
            workerDTO.setOrganizationId(newOrg.getId());
            workerService.createWorker(workerDTO);
        }
        return entities.size();
    }
}
