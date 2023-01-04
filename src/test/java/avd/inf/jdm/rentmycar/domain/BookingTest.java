package avd.inf.jdm.rentmycar.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("dev")
@SpringBootTest
class BookingTest {

    User testOwner1 = new User("Test", "The Owner", "welkom", LocalDate.of(2022,01,01), "test+owner@rentmycar.nl", 100);
    User testCustomer1 = new User("Test", "The Customer", "welkom", LocalDate.of(2022,01,01), "test+customer@rentmycar.nl", 100);
    Car testCar1 = new ICE("AB-12-CD", (short) 2006, "Renault Kangoo", "BLACK", 200000, 5, null,"personenauto",testOwner1);
    Offer testOffer1 = new Offer(LocalDateTime.parse("2022-10-10T08:00"), LocalDateTime.parse("2022-10-10T10:00"), "Rotterdam", testCar1);


    @Test
    @DisplayName("Default BookingStatus is PENDING")
    void Booking_DefaultBookingStatusIsPENDING() {
        Booking testBooking1 = new Booking(testOffer1, testCustomer1);
        assertEquals(BookingStatus.PENDING, testBooking1.getStatus());
    }

    @Test
    @DisplayName("Calculate the bonuspoint system")
    void Booking_BonusPointsShouldCalculateCorrectly() {
        //arrange
        Booking testBooking1 = new Booking(testOffer1, testCustomer1);
        Ride testRide1 = new Ride(testBooking1);
        testBooking1.setRideAsChild(testRide1);
        int expectedBonusPoints = 600;

        //act
        testRide1.setMaxAccelerationForce(100);
        testRide1.setTotalKilometersDriven(100);
        testBooking1.calculateBonusPointsForThisRide();

        int actualBonusPoints = testCustomer1.getBonusPoints();

        //assert
        Assertions.assertEquals(expectedBonusPoints, actualBonusPoints);
    }

    @Test
    @DisplayName("Calculate the bonuspoint system")
    void Booking_BonusPointsShouldCalculateCorrectlyUnexpected() {
        //arrange
        Booking testBooking1 = new Booking(testOffer1, testCustomer1);
        Ride testRide1 = new Ride(testBooking1);
        testBooking1.setRideAsChild(testRide1);
        int unexpectedBonusPoints = 150;

        //act
        testRide1.setMaxAccelerationForce(100);
        testRide1.setTotalKilometersDriven(100);
        testBooking1.calculateBonusPointsForThisRide();

        int actualBonusPoints = testCustomer1.getBonusPoints();

        //assert
        Assertions.assertNotEquals(unexpectedBonusPoints, actualBonusPoints);
    }
}