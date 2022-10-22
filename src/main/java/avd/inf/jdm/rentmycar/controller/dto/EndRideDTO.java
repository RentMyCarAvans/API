package avd.inf.jdm.rentmycar.controller.dto;

import avd.inf.jdm.rentmycar.domain.Booking;
import avd.inf.jdm.rentmycar.domain.BookingStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class EndRideDTO {
    private BookingStatus status;
    private double startRideLatitude;
    private double startRideLongitude;
    private double endRideLatitude;
    private double endRideLongitude;

    private LocalDateTime endDateTime;

    private double totalKilometersDriven;
    private double maxAccelerationForce;
}
