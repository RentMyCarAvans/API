package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.Booking;
import avd.inf.jdm.rentmycar.domain.BookingStatus;
import avd.inf.jdm.rentmycar.domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findOffersByPickupLocation(String city);

    @Query(value = "SELECT * FROM offers AS o WHERE NOT EXISTS (SELECT offer FROM bookings b WHERE o.id = b.offer)", nativeQuery = true)
    List<Offer> findUnbooked();

    Boolean findOfferByCar(Long id);

}
