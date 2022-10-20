package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Optional<User> findUserByEmail(String email);

}
