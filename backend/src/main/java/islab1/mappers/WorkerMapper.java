package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.DTO.WorkerDTO;
import islab1.models.Coordinates;
import islab1.models.Organization;
import islab1.models.Person;
import islab1.models.Worker;
import islab1.models.auth.User;
import islab1.repos.CoordinatesRepo;
import islab1.repos.OrganizationRepo;
import islab1.repos.PersonRepo;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class WorkerMapper {

    private final UserRepo userRepo;
    private final PersonRepo personRepo;
    private final OrganizationRepo organizationRepo;
    private final CoordinatesRepo coordinatesRepo;

    public Worker toEntity(WorkerDTO dto) throws ConvertionException {
        try {
            User user = userRepo.getReferenceById(dto.getCreatorId());
            Coordinates coordinates = coordinatesRepo.getReferenceById(dto.getCoordinatesId());
            Organization organization = organizationRepo.getReferenceById(dto.getOrganizationId());
            Person person = personRepo.getReferenceById(dto.getPersonId());
            Worker worker = new Worker();
            worker.setCreator(user);
            worker.setName(dto.getName());
            worker.setSalary(dto.getSalary());
            worker.setCoordinates(coordinates);
            worker.setPerson(person);
            worker.setOrganization(organization);
            worker.setCreationDate(dto.getCreationDate());
            worker.setStartDate(dto.getStartDate());
            worker.setEndDate(dto.getEndDate());
            worker.setPosition(dto.getPosition());
            worker.setRating(dto.getRating());
            return worker;
        } catch (ConvertionException e) {
            System.out.println(e.getMessage());
            throw e;
        } catch (EntityNotFoundException e) {
            System.out.println(e.getMessage());
            throw new ConvertionException(e.getMessage());
        }
    }

    public WorkerDTO toDto(Worker worker) {
        WorkerDTO dto = new WorkerDTO();
        dto.setId(worker.getId());
        dto.setCreatorId(worker.getCreator().getId());
        dto.setCreationDate(worker.getCreationDate());
        dto.setCoordinatesId(worker.getCoordinates().getId());
        dto.setName(worker.getName());
        dto.setSalary(worker.getSalary());
        dto.setStartDate(worker.getStartDate());
        dto.setEndDate(worker.getEndDate());
        dto.setPosition(worker.getPosition());
        dto.setRating(worker.getRating());
        dto.setPersonId(worker.getPerson().getId());
        dto.setOrganizationId(worker.getOrganization().getId());
        return dto;
    }
}
