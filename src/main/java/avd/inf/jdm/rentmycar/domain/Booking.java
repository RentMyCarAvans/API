package avd.inf.jdm.rentmycar.domain;

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

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ride")
    private Ride ride;

    @OneToOne(fetch = FetchType.EAGER)
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
}
