package islab1.mappers;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Component;

import islab1.exceptions.ConvertionException;
import islab1.models.DTO.PersonDTO;
import islab1.models.Location;
import islab1.models.Person;
import islab1.models.auth.User;
import islab1.repos.LocationRepo;
import islab1.repos.UserRepo;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PersonMapper {

    private final UserRepo userRepo;
    private final LocationRepo locationRepo;

    public Person toEntity(PersonDTO dto) throws ConvertionException {
        try {
            User creator = userRepo.getReferenceById(dto.getCreatorId());
            Location location = locationRepo.getReferenceById(dto.getLocationId());
            Person person = new Person();
            person.setCreator(creator);
            person.setEyeColor(dto.getEyeColor());
            person.setHairColor(dto.getHairColor());
            person.setLocation(location);
            person.setWeight(dto.getWeight());
            
            return person;
        } catch (ConvertionException e) {
            throw e;
        } catch (EntityNotFoundException e) {
            throw new ConvertionException(e.getMessage());
        }
    }

    public PersonDTO toDto(Person person) {
        PersonDTO dto = new PersonDTO();
        dto.setId(person.getId());
        dto.setCreatorId(person.getCreator().getId());
        dto.setEyeColor(person.getEyeColor());
        dto.setHairColor(person.getHairColor());
        dto.setLocationId(person.getLocation().getId());
        dto.setWeight(person.getWeight());
        return dto;
    }
}