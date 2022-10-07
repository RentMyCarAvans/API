package avd.inf.jdm.rentmycar.domain;

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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "offer")
    private Offer offer;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer")
    private User customer;

    @Column(name = "dropOfLocation")
    private String dropOfLocation;

    public Booking(Offer offer, User customer) {
        this.offer = offer;
        this.customer = customer;
    }
}
