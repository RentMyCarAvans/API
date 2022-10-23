package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.RentMyCarApplication;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RentMyCarApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfferControllerTest {
    @LocalServerPort
    private int port;

    final TestRestTemplate restTemplate = new TestRestTemplate();

    final HttpHeaders headers = new HttpHeaders();

    @Test
    public void testRetrieveOfferFive() throws JSONException {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort(),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"data\":{\"id\":5,\"startDateTime\":\"2022-11-01T12:00:00\",\"endDateTime\":\"2022-11-01T13:00:00\",\"pickupLocation\":\"Tilburg\",\"car\":{\"type\":\"BEV\",\"id\":2,\"licensePlate\":\"2BEV34\",\"yearOfManufacture\":2021,\"model\":\"Lamborgini Diablo\",\"colorType\":\"BLACK\",\"mileage\":1000,\"numberOfSeats\":2,\"user\":{\"id\":4,\"email\":\"paul@avans.nl\",\"firstName\":\"Paul\",\"lastName\":\"de Mast\",\"dateOfBirth\":\"2000-01-01\",\"bonusPoints\":100,\"adult\":true,\"admin\":false}}},\"status\":200}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private String createURLWithPort() {
        return "http://localhost:" + port + "/api/v1/offers/5";
    }
}