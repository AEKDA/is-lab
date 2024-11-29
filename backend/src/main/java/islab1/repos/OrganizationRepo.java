package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Organization;
import islab1.models.auth.User;

@Repository
public interface OrganizationRepo extends JpaRepository<Organization, Long> {
    List<Organization> getOrganizationsByCreator(User user);
}
