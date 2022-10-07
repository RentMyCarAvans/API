package avd.inf.jdm.rentmycar.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class UserTest {
@Test
    @DisplayName("Adding user and retrieving user")
        void getAllUsers() {
        //    arrange
        User user1 = new User("Tessie", "De Tester", LocalDate.of(2001,01,01), 100);

        String expectedFirstName = "Tessie";
        String expectedLastName = "De Tester";
        LocalDate expectedDoB =  LocalDate.of(2001,01,01);
        int expectedBonusPoints = 100;

        // act
        String actualFirstName = user1.getFirstName();
        String actualLastName = user1.getLastName();
        LocalDate  actualDoB = user1.getDateOfBirth();
        int actualBonusPoints = user1.getBonusPoints();

        //arrange
        Assertions.assertEquals(expectedFirstName, actualFirstName);
        Assertions.assertEquals(expectedLastName, actualLastName);
        Assertions.assertEquals(expectedDoB, actualDoB);
        Assertions.assertEquals(expectedBonusPoints, actualBonusPoints);

    }

}

