package avd.inf.jdm.rentmycar.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CarServiceTest {

    private CarService carService;

    @Test
    @DisplayName("Adding succesfully a new ICE car")
    void saveCarSuccesfull() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    @DisplayName("Succesful validation of valid licenseplates with different patterns")
    void checkValidLicensePlate() {
        // Arrange

        // Act

        // Assert

    }

    @Test
    @DisplayName("Succesful invalidation of licenseplates not matching any pattern")
    void checkInvalidLicensePlate() {
        // Arrange

        // Act

        // Assert

    }
}
