package avd.inf.jdm.rentmycar.controller.dto;

import avd.inf.jdm.rentmycar.domain.ColorType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarDTO {
    private String type;
    private String licensePlate;
    private Short yearOfManufacture;
    private String model;
    private ColorType colorType;
    private int mileage;
    private int numberOfSeats;
    private long userId;
}

