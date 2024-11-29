package islab1.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import islab1.exceptions.ConvertionException;
import islab1.mappers.PersonMapper;
import islab1.models.DTO.PersonDTO;
import islab1.models.Person;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.PersonRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonMapper personMapper;
    private final PersonRepo personRepo;
    private final LocationService locationService;

    public List<Person> getAllPersons() {
        return personRepo.findAll();
    }

    public List<Person> getPersonsByUser(User user) {
        return personRepo.getPersonsByCreator(user);
    }

    public Person getPersonById(Long id) {
        return personRepo.getById(id);
    }

    public boolean existById(Long id) {
        return personRepo.existsById(id);
    }

    public Person createPerson(PersonDTO personDTO) throws ConvertionException {
        Person person = personMapper.toEntity(personDTO);
        return personRepo.save(person);
    }

    public Person updatePerson(Long id, PersonDTO personDTO) throws ConvertionException, EntityNotFoundException {
        Person newPerson = personMapper.toEntity(personDTO);
        if (!existById(id)) {
            throw new EntityNotFoundException("There is no person with id " + id);
        }
        Person person = getPersonById(id);
        
        newPerson.setId(id);
        newPerson.setCreator(person.getCreator());
        personRepo.save(newPerson);
        return newPerson;
    }

    public void deletePerson(Long id) {
        try{
            personRepo.deleteById(id);
        }catch(Exception e){
            throw e;
        }
    }

    public boolean checkAccess(User user, Long personId) {
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        Person person = getPersonById(personId);
        return person != null && person.getCreator().equals(user);
    }

    public List<PersonDTO> convertPersonsToDTOs(List<Person> persons) {
        return persons.stream()
            .map(personMapper::toDto)
            .collect(Collectors.toList());
    }

    public PersonDTO convertPersonToDTO(Person person) {
        return personMapper.toDto(person);
    }
}
