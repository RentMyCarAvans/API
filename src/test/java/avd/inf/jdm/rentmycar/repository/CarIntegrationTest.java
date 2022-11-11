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
class CarIntegrationTest {

    User testOwner = new User("Jimmy", "Green", "welkom", LocalDate.of(1980,01,01), "jimmy.green@outlook.com", 0);

    Car testCar = new ICE("XX123Z", (short) 2006, "KIA Sportage", ColorType.BLACK, 2500, 5, testOwner);

    @Autowired UserRepository userRepository;
    @Autowired CarRepository carRepository;


    @BeforeAll
    public void setup() {
        userRepository.save(testOwner);
    }

    @Test
    @DisplayName("Car is added to the database")
    void Car_AddingOfferToTheRepository_CountAfterSavingShouldBeOneMore() {
        int count = carRepository.findAll().size();
        carRepository.save(testCar);
        assertEquals(count + 1, carRepository.findAll().size());
    }

    @Test
    @DisplayName("Car is removed from the database")
    void Car_RemovingOfferFromTheRepository_CountAfterDeletingShouldBeOneLess() {
        carRepository.save(testCar);
        int count = carRepository.findAll().size();
        carRepository.delete(testCar);
        assertEquals(count - 1, carRepository.findAll().size());
    }




}