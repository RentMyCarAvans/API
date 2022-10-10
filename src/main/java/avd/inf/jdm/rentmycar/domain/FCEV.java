package avd.inf.jdm.rentmycar.domain;

import javax.persistence.Entity;

@Entity
public class FCEV extends Car{

    public FCEV() {
    }

    public FCEV(String licensePlate, Short yearOfManufacture, String model, ColorType colorType, int mileage, int numberOfSeats/*, User user*/) {
        super(licensePlate, yearOfManufacture, model, colorType, mileage, numberOfSeats/*, user*/);
    }

    @Override
    public double calculateTCO(int mileage, int yearOfManufacture, int numberOfSeats) {
        return 200 + ((mileage/1000) + (yearOfManufacture*0.01) + (numberOfSeats * 1.00));
    }
}
