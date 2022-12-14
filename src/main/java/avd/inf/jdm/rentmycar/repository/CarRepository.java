package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    public Optional<Car> findByLicensePlate (String licensePlate);

    public Boolean existsByLicensePlate(String licensePlate);


}
