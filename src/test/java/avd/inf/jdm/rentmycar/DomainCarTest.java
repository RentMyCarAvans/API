package avd.inf.jdm.rentmycar;

import avd.inf.jdm.rentmycar.domain.Car;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DomainCarTest {

    @Test
    @DisplayName("Adding a new car and retrieving all the details")
    void getAllCars() {

        // Arrange
        Car car1 = new Car("H-777-RR", (short) 2022,"Porsche 911 Carrera GTS","BLACK",0);

        short expectedYearOfManufacture = 2022;
        String expectedLicensePlate = "H-777-RR";
        String expectedModel = "Porsche 911 Carrera GTS";
        String expectedColor ="BLACK";
        int expectedMileage = 0;

        // Act
        short actualYearOfManufacture = car1.getYearOfManufacture();
        String actualLicensePlate = car1.getLicensePlate();
        String actualModel = car1.getModel();
        String actualColor = car1.getColor();
        int actualMileage = car1.getMileage();

        // Assert
        Assertions.assertEquals(expectedYearOfManufacture, actualYearOfManufacture);
        Assertions.assertEquals(expectedLicensePlate, actualLicensePlate);
        Assertions.assertEquals(expectedModel, actualModel);
        Assertions.assertEquals(expectedColor, actualColor);
        Assertions.assertEquals(expectedMileage, actualMileage);
    }

    @Test
    @DisplayName("[TODO] Deleting a car by licenseplate")
    void deleteCarByLicensePlate() {

        // Arrange
        Car car1 = new Car("H-123-SD", (short) 2019,"Kia Sportage","BLACK",12422);

        // Act

        // Assert
    }

    @Test
    @DisplayName("Updating mileage of a car")
    void updateCarWithNewMileage() {

        // Arrange
        Car car1 = new Car("H-123-SD", (short) 2019,"Kia Sportage","BLACK",12422);
        int expectedMileage = 45222;

        // Act
        car1.setMileage(45222);

        // Assert
        Assertions.assertEquals(expectedMileage, car1.getMileage());
    }
}
