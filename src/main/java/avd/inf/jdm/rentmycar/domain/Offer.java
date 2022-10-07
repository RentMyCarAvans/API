package avd.inf.jdm.rentmycar.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "startDateTime", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "endDateTime", columnDefinition = "TIMESTAMP", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "pickupLocation", nullable = false)
    private String pickupLocation;


//    @OneToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "car")
//    private Car car;


    public Offer(LocalDateTime startDateTime, LocalDateTime endDateTime, String pickupLocation) {
        if (startDateTime == null)
        {
            throw new IllegalArgumentException("startDateTime can't be null");
        }

        if (endDateTime == null)
        {
            throw new IllegalArgumentException("endDateTime can't be null");
        }

        if (pickupLocation == null)
        {
            throw new IllegalArgumentException("pickupLocation can't be null");
        }

//



        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.pickupLocation = pickupLocation;
    }

}
