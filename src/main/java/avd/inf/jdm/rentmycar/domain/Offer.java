package avd.inf.jdm.rentmycar.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "startDateTime", columnDefinition = "TIMESTAMP")
    @NonNull
    private LocalDateTime startDateTime;

    @Column(name = "endDateTime", columnDefinition = "TIMESTAMP")
    @NonNull
    private LocalDateTime endDateTime;

    @Column(name = "pickupLocation")
    private String pickupLocation;



    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car")
    @NonNull
    private Car car;

    public void setStartDateTime(LocalDateTime startDateTime) {
        if (startDateTime == null) {
            throw new NullPointerException("StartDateTime can not be null");
        }
        if (endDateTime.isEqual(startDateTime)) {
            throw new IllegalArgumentException("StartDateTime can not be equal to EndDateTime");
        }
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(@NonNull LocalDateTime endDateTime) {
        if (endDateTime == null) {
            throw new NullPointerException("EndDateTime can not be null");
        }
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("EndDateTime can not be before StartDateTime");
        }
        if (endDateTime.isEqual(startDateTime)) {
            throw new IllegalArgumentException("EndDateTime can not be equal to StartDateTime");
        }
        this.endDateTime = endDateTime;
    }

    public void setCar(@NonNull Car car) {
        if (car == null) {
            throw new NullPointerException("Car can not be null");
        }
        this.car = car;
    }

    public Offer(LocalDateTime startDateTime, LocalDateTime endDateTime, String pickupLocation, Car car) {
        // Check if input is valid
        if (startDateTime == null) {
            throw new NullPointerException("StartDateTime can not be null");
        }
        if (endDateTime == null) {
            throw new NullPointerException("EndDateTime can not be null");
        }
        if (car == null) {
            throw new NullPointerException("Car can not be null");
        }
        if (endDateTime.isBefore(startDateTime)) {
            throw new IllegalArgumentException("EndDateTime can not be before StartDateTime");
        }
        if (endDateTime.isEqual(startDateTime)) {
            throw new IllegalArgumentException("EndDateTime can not be equal to StartDateTime");
        }
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.pickupLocation = pickupLocation;
        this.car = car;
    }

}