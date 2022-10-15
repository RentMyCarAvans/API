package avd.inf.jdm.rentmycar.domain;

import avd.inf.jdm.rentmycar.service.UserService;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "rides")
public class Ride {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "id", nullable = false)
    private Long id;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User customer;
    @JsonBackReference
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    private Booking booking;
    private double startRideLatitude;
    private double startRideLongitude;
    private double endRideLatitude;
    private double endRideLongitude;

    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    private double totalKilometersDriven;
    private double maxAccelerationForce;

    public Ride (Booking booking) {
        this.booking = booking;
        if (startDateTime == null) this.startDateTime = LocalDateTime.now();

    }

}
