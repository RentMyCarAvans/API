package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.OfferDTO;
import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.service.CarService;
import avd.inf.jdm.rentmycar.service.OfferService;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "offer-controller", description = "Endpoints to add new offers, delete an offer, retrieve all offers, retrieve specific offer information")
@RestController
@RequestMapping("/api")
@CrossOrigin
public class OfferController {

    private final OfferService offerService;
    private final CarService carService;

    @Autowired
    public OfferController(OfferService offerService, CarService carService) {
        this.offerService = offerService;
        this.carService = carService;
    }

    @Operation(summary = "Retrieving all offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all offers",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Offer.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No Offers found",content = @Content) })
    @GetMapping("/v1/offers")
    public ResponseEntity<Object> getAllOffers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String colorOfCar,
            @RequestParam(required = false) Integer numberOfSeats,
            @RequestParam(required = false) Long userId
    ){

        List<Offer> found;

        found = offerService.getAll();

        if(city != null && !city.isEmpty()){
            found = found.stream().filter(offer -> offer.getPickupLocation().equals(city)).toList();
        }

        if(colorOfCar != null && !colorOfCar.isEmpty()){
            found = found.stream().filter(offer -> offer.getCar().getColorType().toString().equals(colorOfCar)).toList();
        }

        if(numberOfSeats != null && numberOfSeats != 0){
            found = found.stream().filter(offer -> offer.getCar().getNumberOfSeats() >= numberOfSeats).toList();
        }

        if(userId != null && userId != 0){
            found = found.stream().filter(offer -> offer.getCar().getUser().getId() == userId).toList();
        }

        return found.isEmpty()
                ? ResponseHandler.generateResponse("No offers found", HttpStatus.NO_CONTENT, null)
                : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Retrieve an offer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the offer",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Offer.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",content = @Content),
            @ApiResponse(responseCode = "404", description = "Offer not found",content = @Content) })
    @GetMapping("/v1/offers/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id){
        Optional<Offer> found =  offerService.getSingleById(id);
        return found.isEmpty()
                ? ResponseHandler.generateResponse("Offer with id " + id + " not found", HttpStatus.NOT_FOUND, null)
                : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Retrieve all unbooked offers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the offers",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Offer.class)) }),
            @ApiResponse(responseCode = "404", description = "Offers not found",content = @Content) })
    @GetMapping("/v1/offers/unbooked")
    public ResponseEntity<Object> getAllUnbookedOffers() {
        try {
            List<Offer> found = new ArrayList<>(offerService.getUnbooked());
            return found.isEmpty()
                    ? ResponseHandler.generateResponse("No offers found", HttpStatus.NO_CONTENT, null)
                    : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Add a new offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Offer created",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Offer.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",content = @Content),
            @ApiResponse(responseCode = "409", description = "Offer already exists",content = @Content) })
    @PostMapping("/v1/offers")
    public ResponseEntity<Object> create(@RequestBody OfferDTO offerDTO) {
        try {
            Car car = carService.getSingleById(offerDTO.getCarId()).isPresent() ? carService.getSingleById(offerDTO.getCarId()).get() : null;
            if(car == null){
                return ResponseHandler.generateResponse("Car with id " + offerDTO.getCarId() + " not found", HttpStatus.NOT_FOUND, null);
            }

            System.out.println(" ========== offer.getCar().getId(): " + car.getId() + " ==========");

            // Check if an offer for that car already exists in the same time period
            List<Offer> offers = offerService.getAll();
            for(Offer offer : offers){
                if(offer.getCar().getId().equals(offerDTO.getCarId())){
                    if(
                            (offer.getStartDateTime().isBefore(offerDTO.getStartDateTime()) && offer.getEndDateTime().isAfter(offerDTO.getStartDateTime()))
                                    || (offer.getStartDateTime().isBefore(offerDTO.getEndDateTime()) && offer.getEndDateTime().isAfter(offerDTO.getEndDateTime()))
                                    || (offer.getStartDateTime().isEqual(offerDTO.getStartDateTime()))
                                    || (offer.getEndDateTime().isEqual(offerDTO.getEndDateTime()))
                    ){
                        return ResponseHandler.generateResponse("Offer already exists for that car in the same time period", HttpStatus.CONFLICT, null);
                    }
                }
            }

            // Get the Latitude and Longitude of the pickup location, using http://api.positionstack.com/v1/forward?access_key=04e13b946c065785becd602f43c14ece&query=
            String url = "http://api.positionstack.com/v1/forward?access_key=04e13b946c065785becd602f43c14ece&query=" + offerDTO.getPickupLocation();
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            // Convert String to JSON Object
            JSONObject locationObj = new JSONObject(result);

            // Get the latitude and longitude from the data in the JSON object
            Double latitude = locationObj.getJSONArray("data").getJSONObject(0).getDouble("latitude");
            Double longitude = locationObj.getJSONArray("data").getJSONObject(0).getDouble("longitude");


            System.out.println(" ~~~~~~ Get LatLng of Offer pickup location: " + result + " ~~~~~~");
            System.out.println(" ~~~~~~ Get LatLng of Offer locationObj: latitude " + latitude + " ~~~~~~");
            System.out.println(" ~~~~~~ Get LatLng of Offer locationObj: longitude " + longitude + " ~~~~~~");


            Offer offer = new Offer();
            offer.setCar(car);
            offer.setPickupLocation(offerDTO.getPickupLocation());
            offer.setStartDateTime(offerDTO.getStartDateTime());
            offer.setEndDateTime(offerDTO.getEndDateTime());
            offer.setPickupLocationLatitude(latitude);
            offer.setPickupLocationLongitude(longitude);


            Offer newOffer = offerService.create(offer);
            if (newOffer != null) {
                return ResponseHandler.generateResponse("New Offer added succesfully", HttpStatus.CREATED, newOffer);
            }

            return ResponseHandler.generateResponse("Offer could not be created", HttpStatus.BAD_REQUEST, null);

        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    @Operation(summary = "Update an offer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer updated", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Offer.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Offer not found", content = @Content) })
    @PutMapping("/v1/offers/{id}")
    ResponseEntity<Object> update(@RequestBody OfferDTO offerDTO, @PathVariable Long id) throws JSONException {
        Optional<Offer> optionalOffer = offerService.getSingleById(id);

        Car car = carService.getSingleById(offerDTO.getCarId()).isPresent() ? carService.getSingleById(offerDTO.getCarId()).get() : null;
        if(car == null){
            return ResponseHandler.generateResponse("Car with id " + offerDTO.getCarId() + " not found", HttpStatus.NOT_FOUND, null);
        }

        if (optionalOffer.isPresent()) {

            Offer offer = optionalOffer.get();

            offer.setStartDateTime(offerDTO.getStartDateTime());
            offer.setEndDateTime(offerDTO.getEndDateTime());
            offer.setPickupLocation(offerDTO.getPickupLocation());
            offer.setCar(car);


            // Get the Latitude and Longitude of the pickup location, using http://api.positionstack.com/v1/forward?access_key=04e13b946c065785becd602f43c14ece&query=
            String url = "http://api.positionstack.com/v1/forward?access_key=04e13b946c065785becd602f43c14ece&query=" + offerDTO.getPickupLocation();
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(url, String.class);
            // Convert String to JSON Object
            JSONObject locationObj = new JSONObject(result);

            // Get the latitude and longitude from the data in the JSON object
            Double latitude = locationObj.getJSONArray("data").getJSONObject(0).getDouble("latitude");
            Double longitude = locationObj.getJSONArray("data").getJSONObject(0).getDouble("longitude");


            System.out.println(" ~~~~~~ Get LatLng of Offer pickup location: " + result + " ~~~~~~");
            System.out.println(" ~~~~~~ Get LatLng of Offer locationObj: latitude " + latitude + " ~~~~~~");
            System.out.println(" ~~~~~~ Get LatLng of Offer locationObj: longitude " + longitude + " ~~~~~~");

            offer.setPickupLocationLatitude(latitude);
            offer.setPickupLocationLongitude(longitude);



            return ResponseHandler.generateResponse("Offer with id " + id + " is succesfully updated", HttpStatus.OK, offerService.save(offer));

        }
        return ResponseHandler.generateResponse("Offer with id " + id + " could not be found", HttpStatus.NOT_FOUND, null);
    }

    @Operation(summary = "Delete an offer by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Offer deleted",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Offer.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",content = @Content),
            @ApiResponse(responseCode = "404", description = "Offer not found",content = @Content) })
    @DeleteMapping("/v1/offers/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        // 1. Check if the offer exists
        // 2. Check is the offer is booked
        // 3a. If the offer exists and is not booked, delete it and return 200
        // 3b. If the offer exists and is booked, return a 400
        // 3c. If the offer does not exist, return a 404

        Optional<Offer> optionalOffer = offerService.getSingleById(id);

        if (optionalOffer.isPresent()) {
            Offer offer = optionalOffer.get();

            if (!offerService.offerIsBooked(offer)) {
                offerService.delete(offer);
                return ResponseHandler.generateResponse("Offer with id " + id + " is succesfully deleted", HttpStatus.OK, null);
            } else {
                return ResponseHandler.generateResponse("Offer is booked and can not be deleted", HttpStatus.BAD_REQUEST, null);
            }

        }
        return ResponseHandler.generateResponse("Offer with id " + id + " not found", HttpStatus.NOT_FOUND, null);
    }

}
