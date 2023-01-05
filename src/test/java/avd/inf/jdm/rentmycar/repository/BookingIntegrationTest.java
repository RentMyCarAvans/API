package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("dev")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookingIntegrationTest {

    User testOwner1 = new User("Tessy", "De Owner", "welkom", LocalDate.of(2022,01,01), "tessy@avans.nl", 100);
    User testCustomer = new User("Testy", "De Customer", "welkom", LocalDate.of(2022,01,01), "email@test.test", 100);
    Car testCar1 = new ICE("AB-12-CD", (short) 2006, "Renault Kangoo", "BLACK", 200000, 5, null,"personenauto",testOwner1);
    Offer testOffer1 = new Offer(LocalDateTime.parse("2022-10-10T08:00"), LocalDateTime.parse("2022-10-10T10:00"), "Rotterdam", testCar1);

    @Autowired UserRepository userRepository;
    @Autowired CarRepository carRepository;
    @Autowired OfferRepository offerRepository;
    @Autowired BookingRepository bookingRepository;

    @BeforeAll
    public void setup() {
        userRepository.save(testOwner1);
        userRepository.save(testCustomer);
        carRepository.save(testCar1);
        offerRepository.save(testOffer1);
    }

    @Test
    @DisplayName("Booking is added to the repository")
    void Booking_AddingBookingToTheRepository_CountAfterSavingShouldBeOneMore() {
        int numberOfBookings = bookingRepository.findAll().size();
        Booking testBooking = new Booking(testOffer1, testCustomer);
        bookingRepository.save(testBooking);
        assertEquals(numberOfBookings + 1, bookingRepository.findAll().size());
    }

    @Test
    @DisplayName("Booking is removed from the repository")
    void Booking_RemovingBookingFromRepository_CountAfterRemovingShouldBeOneLess() {
        Booking testBooking = new Booking(testOffer1, testCustomer);
        bookingRepository.save(testBooking);
        int numberOfBookings = bookingRepository.findAll().size();
        bookingRepository.delete(testBooking);
        assertEquals(numberOfBookings - 1, bookingRepository.findAll().size());
    }
}