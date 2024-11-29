package islab1.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.auth.AdminRequest;

@Repository
public interface AdminRequestRepo extends JpaRepository<AdminRequest, Long> {
    AdminRequest getAdminRequestByUsername(String username);
}
