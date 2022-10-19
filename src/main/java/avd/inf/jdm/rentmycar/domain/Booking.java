package avd.inf.jdm.rentmycar.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "offer", nullable = false)
    private Offer offer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "ride")
    private Ride ride;
    @JsonManagedReference

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "customer", nullable = false)
    private User customer;

    @Column(name = "dropOfLocation", nullable = true)
    private String dropOfLocation;

    @Column(name = "status", nullable = false)
    private BookingStatus status;

    public Booking(Offer offer, User customer) {
        if(offer == null) {
            throw new NullPointerException("Offer can not be null");
        }
        if(customer == null) {
            throw new NullPointerException("Customer can not be null");
        }
        this.offer = offer;
        this.customer = customer;
        this.status = BookingStatus.PENDING;
    }

    public Booking(Offer offer, Ride ride, User customer) {
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
