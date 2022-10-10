package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
