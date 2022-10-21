package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.repository.CarRepository;
import avd.inf.jdm.rentmycar.repository.OfferRepository;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import avd.inf.jdm.rentmycar.service.CarService;
import avd.inf.jdm.rentmycar.service.OfferService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OfferControllerTest {

    OfferController offerController;

    @Autowired OfferService offerService;
    @Autowired CarService carService;



    @BeforeEach
    void setUp(){
        offerController = new OfferController(offerService, carService);
    }

    @Test
    void shouldReturnAllOffers()  {
        // arrange

        // act
        var result = offerController.getAllOffers(null, null, null);
        var data = (List<Offer>) ((HashMap) result.getBody()).get("data");

        // assert
        assertEquals(6, data.size());
    }

    @Test
    void shouldReturnOfferWithValidId() {
        // arrange
        var offer = offerController.getById(1L);
        System.out.println(offer);

        assertNotNull(offer);
    }


}