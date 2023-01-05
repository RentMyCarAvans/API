package avd.inf.jdm.rentmycar.domain;

import javax.persistence.Entity;

@Entity
public class ICE extends Car{

    public ICE() {
    }

    public ICE(String licensePlate, Short yearOfManufacture, String model, String colorType, int mileage, int numberOfSeats, String image, String vehicleType,  User user) {
        super(licensePlate, yearOfManufacture, model, colorType, mileage, numberOfSeats, image, vehicleType, user);
    }

   @Override
    public double calculateTCO(int mileage, int yearOfManufacture, int numberOfSeats) {
        double tco;
        tco = 100 + ((mileage/1000) + (yearOfManufacture*0.01) + (numberOfSeats * 2.50));
        return Math.round(tco*100.0)/100.0;
    }

}
