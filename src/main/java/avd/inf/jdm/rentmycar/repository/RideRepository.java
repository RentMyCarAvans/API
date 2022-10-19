package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride, Long> {

    @Query(value = "SELECT * FROM rides INNER JOIN bookings on rides.booking_id = bookings.id INNER JOIN offers on bookings.offer = offers.id WHERE CAR = :carId", nativeQuery = true)
    List<Ride> findRidesByCarId(@Param("carId")Long carId);
}
