package avd.inf.jdm.rentmycar.controller.dto;

import avd.inf.jdm.rentmycar.domain.Offer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OfferDTO {
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String pickupLocation;
    private long carId;
}
