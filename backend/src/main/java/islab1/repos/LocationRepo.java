package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Location;
import islab1.models.auth.User;

@Repository
public interface LocationRepo extends JpaRepository<Location, Long>{

    List<Location> getLocationsByCreator(User user);
    
}
