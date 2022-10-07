package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OfferRepository extends JpaRepository<Offer, Long> {

    List<Offer> findOffersByPickupLocation(String city);

    // TODO RS: Check if this works after inplementing the bookings-subsystem
    @Query(value = "SELECT * FROM offers AS o WHERE o.id NOT EXISTS (SELECT offer FROM bookings)", nativeQuery = true)
    List<Offer> findUnbooked();

}
