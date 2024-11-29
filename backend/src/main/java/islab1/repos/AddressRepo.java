package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Address;
import islab1.models.auth.User;

@Repository
public interface AddressRepo extends JpaRepository<Address, Long> {

    List<Address> getAddresssByCreator(User user);

}
