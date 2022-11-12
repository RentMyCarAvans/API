package avd.inf.jdm.rentmycar.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("dev")

@DisplayName("Offer model")
@SpringBootTest
class OfferTest {

    User testOwner1 = new User("Tessy", "De Tester", "welkom", LocalDate.of(2022,01,01), "tessy@avans.nl", 100);
    Car testCar1 = new ICE("AB-12-CD", (short) 2006, "Renault Kangoo", ColorType.BLACK, 200000, 5, testOwner1);
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
    @DisplayName("EndDateTime can not be set null using setter")
    void Offer_EndDateTimeCanNotBeNullUsingSetter_ExceptionThrown() {
        assertThrows(NullPointerException.class, () -> testOffer1.setEndDateTime(null));
    }

    @Test
    @DisplayName("EndDateTime can not be before StartDateTime")
    void Offer_EndDateTimeCanNotBeBeforeStartDateTime_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Offer(LocalDateTime.now(), LocalDateTime.now().minusHours(1), "Rotterdam", testCar1));
    }

    @Test
    @DisplayName("EndDateTime can not be equal to StartDateTime")
    void Offer_EndDateTimeCanNotBeEqualToStartDateTime_ExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> new Offer(LocalDateTime.parse("2022-10-10T10:00"), LocalDateTime.parse("2022-10-10T10:00"), "Rotterdam", testCar1));
    }



}