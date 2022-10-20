package avd.inf.jdm.rentmycar.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserTest {

@Test
@DisplayName("Creating a user and retrieving their details")
    void getUser() {

    // arrange
    User testUser1 = new User("Test", "The Tester", "welkom", LocalDate.of(2022,01,01), "test+thetester@rentmycar.nl", 100);

    String expectedEmail = "test+thetester@rentmycar.nl";
    Boolean expectedAdult = false;
    int expectedBonusPoint = 100;

    // act
    String actualEmail = testUser1.getEmail();
    Boolean actualAdult = testUser1.isAdult();
    int actualBonusPoints = testUser1.getBonusPoints();

    // assert

    Assertions.assertEquals(expectedAdult, actualAdult);
    Assertions.assertEquals(expectedBonusPoint, actualBonusPoints);
    Assertions.assertEquals(expectedEmail, actualEmail);


}

@Test
@DisplayName("Test for method isAdult")
void User_IsAdultMethodWorksAsIntended() {
    //arrange
    User minorUser = new User("Tessy", "De Tester", "welkom", LocalDate.now().minusYears(12), "tessy@avans.nl", 100);
    User adultUser = new User("Tessy", "De Tester", "welkom", LocalDate.now().minusYears(18), "tessy@avans.nl", 100);

    boolean minorUserExpected = false;
    boolean adultUserExpected = true;

    // act
    Boolean actualMinorUser = minorUser.isAdult();
    Boolean actualAdultUser = adultUser.isAdult();


    //assert
    Assertions.assertEquals(minorUserExpected, actualMinorUser);
    Assertions.assertEquals(adultUserExpected, actualAdultUser);
}

    @Test
    @DisplayName("Customer can not be set null on initialization")
    void User_LastNameCannotBeNull() {
        assertThrows(NullPointerException.class, () -> new User("Test", null, null, null, null, 0));
    }


}
