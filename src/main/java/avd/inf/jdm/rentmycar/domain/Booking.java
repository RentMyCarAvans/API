package avd.inf.jdm.rentmycar.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "bookings")
public class Booking {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    @NonNull
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "offer")
    @NonNull
    private Offer offer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ride")
    private Ride ride;
    @JsonManagedReference

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer")
    @NonNull
    private User customer;

    @Column(name = "dropOfLocation")
    private String dropOfLocation;

    @Column(name = "status")
    @NonNull
    private BookingStatus status;

    public Booking(@NonNull Offer offer, @NonNull User customer) {
        this.offer = offer;
        this.customer = customer;
        this.status = BookingStatus.PENDING;
    }

    public Booking(@NonNull Offer offer, @NonNull Ride ride, @NonNull User customer) {
        this.offer = offer;
        this.ride = ride;
        this.customer = customer;
    }

    public void setRideAsChild(Ride ride) {
            this.ride = ride;
    }
    public void calculateBonusPointsForThisRide() {
        Ride ride = this.getRide();
        double calculateRecklessness =  100 + ((ride.getTotalKilometersDriven()*1.5) + (ride.getMaxAccelerationForce()*10));
        User driver = this.customer;
        int driversAge = driver.calculateAge();

        // drivers below age 30 get bigger penalty
        double recklessnessToPoints = calculateRecklessness / (driversAge < 30 ? 2.5 : 2) ;

        driver.setBonusPoints(driver.getBonusPoints() + (int)recklessnessToPoints);

    };
}
