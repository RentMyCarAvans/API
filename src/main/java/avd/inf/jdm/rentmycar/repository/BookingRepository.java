package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.Booking;
import avd.inf.jdm.rentmycar.domain.BookingStatus;
import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findBookingsByCustomer(User customer);
    Optional<Booking> findBookingByOffer(Offer offer);

    Optional<List<Booking>> findBookingByStatus(BookingStatus status);
}