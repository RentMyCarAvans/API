package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
