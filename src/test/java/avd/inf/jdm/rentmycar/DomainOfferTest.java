package avd.inf.jdm.rentmycar;

import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Offer model")
@SpringBootTest
class DomainOfferTest {

    User testOwner1 = new User();

    Car testCar1 = new Car("AB-12-CD", (short) 2006, "Renault Kangoo", "Blue", 200000);
    Offer testOffer1 = new Offer(LocalDateTime.parse("2022-10-10T08:00"), LocalDateTime.parse("2022-10-10T10:00"), "Rotterdam", testCar1);




    @Test
    @DisplayName("StartDateTime can not be set null on initialization")
    void Offer_StartDateTimeCanNotBeNullOnInitialization_ExceptionThrown() {
        assertThrows(NullPointerException.class, () -> new Offer(null, LocalDateTime.parse("2022-10-10T10:00"), "Rotterdam", testCar1));
    }

    @Test
    @DisplayName("EndDateTime can not be set null on initialization")
    void Offer_EndDateTimeCanNotBeNullOnInitialization_ExceptionThrown() {
        assertThrows(NullPointerException.class, () -> new Offer(LocalDateTime.now(), null, "Rotterdam", testCar1));
    }

    @Test
    @DisplayName("Car can not be set null on initialization")
    void Offer_CarCanNotBeNullOnInitialization_ExceptionThrown() {
        assertThrows(NullPointerException.class, () -> new Offer(LocalDateTime.now(), LocalDateTime.now().plusHours(1), "Rotterdam", null));
    }

    @Test
    @DisplayName("StartDateTime can not be set null using setter")
    void Offer_StartDateTimeCanNotBeNullUsingSetter_ExceptionThrown() {
        assertThrows(NullPointerException.class, () -> testOffer1.setStartDateTime(null));
    }

    @Test
    @DisplayName("EndDateTime can not be set null using setter")
    void Offer_EndDateTimeCanNotBeNullUsingSetter_ExceptionThrown() {
        assertThrows(NullPointerException.class, () -> testOffer1.setEndDateTime(null));
    }

    @Test
    @DisplayName("Car can not be set null using setter")
    void Offer_CarCanNotBeNullUsingSetter_ExceptionThrown() {
        assertThrows(NullPointerException.class, () -> testOffer1.setCar(null));
    }

    @Test
    @DisplayName("EndDateTime can not be before StartDateTime")
    void Offer_EndDateTimeCanNotBeBeforeStartDateTime_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Offer(LocalDateTime.now(), LocalDateTime.now().minusHours(1), "Rotterdam", testCar1));
    }

//    @Test
//    @DisplayName("EndDateTime can not be the same as StartDateTime")
//    void Offer_EndDateTimeCanNotBeTheSameAsStartDateTime_ExceptionThrown() {
//        assertThrows(IllegalArgumentException.class, () -> new Offer(LocalDateTime.now(), LocalDateTime.now(), "Rotterdam", testCar1));
//    }



}