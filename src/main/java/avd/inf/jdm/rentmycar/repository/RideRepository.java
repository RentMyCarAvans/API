package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @Query(value = "SELECT * FROM rides", nativeQuery = true)
    List<Ride> findRidesByCarId(Long carId);
}
