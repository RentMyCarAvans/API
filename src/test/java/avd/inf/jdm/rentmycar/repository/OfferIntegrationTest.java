package avd.inf.jdm.rentmycar.repository;

import avd.inf.jdm.rentmycar.domain.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@Disabled

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OfferIntegrationTest {


    User testOwner1 = new User("Tessy", "De Tester", "welkom", LocalDate.of(2022,01,01), "tessy@avans.nl", 100);

    Car testCar1 = new ICE("AB-12-CD", (short) 2006, "Renault Kangoo", ColorType.BLACK, 200000, 5, testOwner1);
    Offer testOffer1 = new Offer(LocalDateTime.parse("2023-10-10T08:00"), LocalDateTime.parse("2023-10-10T10:00"), "Rotterdam", testCar1);


    @Autowired OfferRepository offerRepository;
    @Autowired UserRepository userRepository;
    @Autowired CarRepository carRepository;


    @BeforeAll
    public void setup() {
        userRepository.save(testOwner1);
        carRepository.save(testCar1);
    }

    @Test
    @DisplayName("Offer is added to the database")
    void Offer_AddingOfferToTheRepository_CountAfterSavingShouldBeOneMore() {
        int count = offerRepository.findAll().size();
        offerRepository.save(testOffer1);
        assertEquals(count + 1, offerRepository.findAll().size());
    }

    @Test
    @DisplayName("Offer is removed from the database")
    void Offer_RemovingOfferFromTheRepository_CountAfterDeletingShouldBeOneLess() {
        offerRepository.save(testOffer1);
        int count = offerRepository.findAll().size();
        offerRepository.delete(testOffer1);
        assertEquals(count - 1, offerRepository.findAll().size());
    }




}