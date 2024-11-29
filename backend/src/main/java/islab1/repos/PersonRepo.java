package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Person;
import islab1.models.auth.User;

@Repository
public interface PersonRepo extends JpaRepository<Person, Long>{

    List<Person> getPersonsByCreator(User user);
    
}
