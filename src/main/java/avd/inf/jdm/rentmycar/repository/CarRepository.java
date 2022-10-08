package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
}
