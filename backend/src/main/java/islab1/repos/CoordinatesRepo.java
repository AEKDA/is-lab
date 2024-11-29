package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Coordinates;
import islab1.models.auth.User;

@Repository
public interface CoordinatesRepo extends JpaRepository<Coordinates, Long> {
    List<Coordinates> getCoordinatesByCreator(User user);
}
