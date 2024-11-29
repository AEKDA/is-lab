package islab1.repos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.auth.Role;
import islab1.models.auth.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long>{
    Optional<User> findByUsername(String username);
    User getUserByUsername(String username);
    List<User> getUsersByRole(Role role);
    Boolean existsByUsername(String username);
}
