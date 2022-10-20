package avd.inf.jdm.rentmycar.integration;

import avd.inf.jdm.rentmycar.domain.*;
import avd.inf.jdm.rentmycar.repository.RideRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")

public class RideRepositoryIntegrationTests {

    @Autowired
    private RideRepository rideRepository;

    User testOwner1 = new User("Test", "The Owner", "welkom", LocalDate.of(2022,01,01), "test+owner@rentmycar.nl", 100);

    User testCustomer1 = new User("Test", "The Customer", "welkom", LocalDate.of(2022,01,01), "test+customer@rentmycar.nl", 100);

    Car testCar1 = new ICE("AB-12-CD", (short) 2006, "Renault Kangoo", ColorType.BLACK, 200000, 5, testOwner1);

    Offer testOffer1 = new Offer(LocalDateTime.parse("2022-10-10T08:00"), LocalDateTime.parse("2022-10-10T10:00"), "Rotterdam", testCar1);
    Ride ride1 = new Ride();

    Booking booking1 = new Booking(testOffer1, ride1,  testCustomer1);






    @Test
    public void whenAddingBookingFromRepository_MoreRidesForCarShouldExist() {
    //arrange
        List<Ride> ridesBefore  = rideRepository.findRidesByCarId(testCar1.getId());
    long countBefore = ridesBefore.stream().count();

    //act
    rideRepository.save(ride1);

    //assert
        assertThat(rideRepository.findRidesByCarId(testCar1.getId())).isEqualTo(countBefore + 1);

    }
}
