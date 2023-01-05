package avd.inf.jdm.rentmycar.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CarDTO {
    private String type;
    private String licensePlate;
    private Short yearOfManufacture;
    private String model;
    private String colorType;
    private int mileage;
    private int numberOfSeats;
    private String image;
    private String vehicleType;
    private long userId;
}

