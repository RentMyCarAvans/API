package avd.inf.jdm.rentmycar.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class BookingTest {

    User testOwner1 = new User("Test", "The Owner", "welkom", LocalDate.of(2022,01,01), "test+owner@rentmycar.nl", 100);
    User testCustomer1 = new User("Test", "The Customer", "welkom", LocalDate.of(2022,01,01), "test+customer@rentmycar.nl", 100);
    Car testCar1 = new ICE("AB-12-CD", (short) 2006, "Renault Kangoo", ColorType.BLACK, 200000, 5, testOwner1);
    Offer testOffer1 = new Offer(LocalDateTime.parse("2022-10-10T08:00"), LocalDateTime.parse("2022-10-10T10:00"), "Rotterdam", testCar1);


    @Test
    @DisplayName("Default BookingStatus is PENDING")
    void Booking_DefaultBookingStatusIsPENDING() {
        Booking testBooking1 = new Booking(testOffer1, testCustomer1);
        assertEquals(BookingStatus.PENDING, testBooking1.getStatus());
    }

}