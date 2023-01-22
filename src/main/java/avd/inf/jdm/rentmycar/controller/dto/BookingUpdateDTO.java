package avd.inf.jdm.rentmycar.controller.dto;

import avd.inf.jdm.rentmycar.domain.BookingStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingUpdateDTO {
    private String dropOfLocation;
    private BookingStatus status;
}
