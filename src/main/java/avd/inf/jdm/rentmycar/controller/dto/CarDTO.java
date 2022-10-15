package avd.inf.jdm.rentmycar.controller.dto;

import avd.inf.jdm.rentmycar.domain.ColorType;
import avd.inf.jdm.rentmycar.service.CarService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
public class CarDTO {
    private static final Logger log = LoggerFactory.getLogger((CarService.class));

    private String licensePlate;
    private Short yearOfManufacture;
    private String model;
    private ColorType colorType;
    private int mileage;
    private int numberOfSeats;
    private long userId;
}

