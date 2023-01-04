package avd.inf.jdm.rentmycar.domain;

import javax.persistence.Entity;
import java.time.YearMonth;

@Entity
public class BEV extends Car{

    public BEV() {
    }

    public BEV(String licensePlate, Short yearOfManufacture, String model, String colorType, int mileage, int numberOfSeats,  String image, String vehicleType, User user) {
        super(licensePlate, yearOfManufacture, model, colorType, mileage, numberOfSeats, image, vehicleType, user);
    }

    @Override
    public double calculateTCO(int mileage, int yearOfManufacture, int numberOfSeats) {
        int amount = 0;
        double tco;

        // Determine the amount based on the year of manufacture. If the car is older than 4 years, then the amount is 500.
        switch (YearMonth.now().getYear() - yearOfManufacture){
            case 0 :
                amount = 100;
            break;
            case 1:
                amount = 200;
            break;
            case 2:
                amount = 300;
                break;
            case 3:
                amount = 400;
                break;
            default:
                amount = 500;
        }
        tco = 300 + ((mileage/1000) + amount + (numberOfSeats * 1.50));
        return Math.round(tco*100.0)/100.0;
    }

}
