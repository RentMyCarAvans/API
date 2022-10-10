package avd.inf.jdm.rentmycar.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DomainCarTest {

    @Test
    @DisplayName("Adding a new ICE car and retrieving all the details")
    void getICECar() {

        // Arrange
        Car car1 = new ICE("H-777-RR", (short) 2022,"Porsche 911 Carrera GTS",ColorType.BLACK,0,2);
        short expectedYearOfManufacture = 2022;
        String expectedLicensePlate = "H-777-RR";
        String expectedModel = "Porsche 911 Carrera GTS";
        String expectedColor ="BLACK";
        int expectedMileage = 0;

        // Act
        short actualYearOfManufacture = car1.getYearOfManufacture();
        String actualLicensePlate = car1.getLicensePlate();
        String actualModel = car1.getModel();
        String actualColor = car1.getColorType();
        int actualMileage = car1.getMileage();

        // Assert
        Assertions.assertEquals(expectedYearOfManufacture, actualYearOfManufacture);
        Assertions.assertEquals(expectedLicensePlate, actualLicensePlate);
        Assertions.assertEquals(expectedModel, actualModel);
        Assertions.assertEquals(expectedColor, actualColor);
        Assertions.assertEquals(expectedMileage, actualMileage);
    }

    @Test
    @DisplayName("Adding a new BEV car and retrieving all the details")
    void getBEVCar() {

        // Arrange
        Car car1 = new BEV("H-777-RR", (short) 2022,"Porsche 911 Carrera GTS",ColorType.BLACK,0,2);
        short expectedYearOfManufacture = 2022;
        String expectedLicensePlate = "H-777-RR";
        String expectedModel = "Porsche 911 Carrera GTS";
        String expectedColor ="BLACK";
        int expectedMileage = 0;

        // Act
        short actualYearOfManufacture = car1.getYearOfManufacture();
        String actualLicensePlate = car1.getLicensePlate();
        String actualModel = car1.getModel();
        String actualColor = car1.getColorType();
        int actualMileage = car1.getMileage();

        // Assert
        Assertions.assertEquals(expectedYearOfManufacture, actualYearOfManufacture);
        Assertions.assertEquals(expectedLicensePlate, actualLicensePlate);
        Assertions.assertEquals(expectedModel, actualModel);
        Assertions.assertEquals(expectedColor, actualColor);
        Assertions.assertEquals(expectedMileage, actualMileage);
    }

    @Test
    @DisplayName("Adding a new FCEV car and retrieving all the details")
    void getFCEVCar() {

        // Arrange
        Car car1 = new FCEV("H-777-RR", (short) 2022,"Porsche 911 Carrera GTS",ColorType.BLACK,0,2);
        short expectedYearOfManufacture = 2022;
        String expectedLicensePlate = "H-777-RR";
        String expectedModel = "Porsche 911 Carrera GTS";
        String expectedColor ="BLACK";
        int expectedMileage = 0;

        // Act
        short actualYearOfManufacture = car1.getYearOfManufacture();
        String actualLicensePlate = car1.getLicensePlate();
        String actualModel = car1.getModel();
        String actualColor = car1.getColorType();
        int actualMileage = car1.getMileage();

        // Assert
        Assertions.assertEquals(expectedYearOfManufacture, actualYearOfManufacture);
        Assertions.assertEquals(expectedLicensePlate, actualLicensePlate);
        Assertions.assertEquals(expectedModel, actualModel);
        Assertions.assertEquals(expectedColor, actualColor);
        Assertions.assertEquals(expectedMileage, actualMileage);
    }

    @Test
    @DisplayName("Calcutating tco of an ICE car")
    void calculateTCOOfICE() {

        // Arrange
        Car car = new ICE("H-777-RR", (short) 2022,"Porsche 911 Carrera GTS",ColorType.BLACK,0,2);
        double expectedTCO = 125.22;

        // Act
        double actualTCO = car.calculateTCO(car.getMileage(), car.getYearOfManufacture(),car.getNumberOfSeats());

        // Assert
        Assertions.assertEquals(expectedTCO, actualTCO);
    }

    @Test
    @DisplayName("Calcutating tco of an BEV car")
    void calculateTCOOfBEV() {

        // Arrange
        Car car = new BEV("H-777-RR", (short) 2022,"Porsche 911 Carrera GTS",ColorType.BLACK,0,2);
        double expectedTCO = 403.0;

        // Act
        double actualTCO = car.calculateTCO(car.getMileage(), car.getYearOfManufacture(),car.getNumberOfSeats());

        // Assert
        Assertions.assertEquals(expectedTCO, actualTCO);
    }

    @Test
    @DisplayName("Calcutating tco of an FCEV car")
    void calculateTCOOfFCEV() {

        // Arrange
        Car car = new FCEV("H-777-RR", (short) 2022,"Porsche 911 Carrera GTS",ColorType.BLACK,0,2);
        double expectedTCO = 222.22;

        // Act
        double actualTCO = car.calculateTCO(car.getMileage(), car.getYearOfManufacture(),car.getNumberOfSeats());

        // Assert
        Assertions.assertEquals(expectedTCO, actualTCO);
    }

    @Test
    @DisplayName("Updating ICE Car with new properties")
    void updateCarWithProperties() {

        // Arrange
        Car car = new ICE("H-123-SD", (short) 2019,"KIA Sportage",ColorType.BLACK,12422,2);
        int expectedMileage = 45222;
        Long expectedId = 10L;
        String expectedLicensePlate = "RF-BT-65";
        String expectedModel = "KIA Ceed";
        String expectedColor = "GREY";
        short expectedYearOfmanufacture = 2007;

        // Act
        car.setLicensePlate("RF-BT-65");
        car.setMileage(45222);
        car.setId(10L);
        car.setModel("KIA Ceed");
        car.setColorType("GREY");
        car.setYearOfManufacture((short) 2007);

        // Assert
        Assertions.assertEquals(expectedMileage, car.getMileage());
        Assertions.assertEquals(expectedId, car.getId());
        Assertions.assertEquals(expectedLicensePlate, car.getLicensePlate());
        Assertions.assertEquals(expectedModel, car.getModel());
        Assertions.assertEquals(expectedColor, car.getColorType());
        Assertions.assertEquals(expectedYearOfmanufacture, car.getYearOfManufacture());
    }
}
