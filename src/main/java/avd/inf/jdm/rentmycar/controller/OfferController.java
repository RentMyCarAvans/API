package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.OfferDTO;
import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.service.CarService;
import avd.inf.jdm.rentmycar.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        @RequestParam(required = false) Integer numberOfSeats
    ){

        List<Offer> found = new ArrayList<>();

        found = offerService.getAll();

        if(city != null && !city.isEmpty()){
            found = found.stream().filter(offer -> offer.getPickupLocation().equals(city)).toList();
        }

        if(colorOfCar != null && !colorOfCar.isEmpty()){
            found = found.stream().filter(offer -> offer.getCar().getColorType().equals(colorOfCar)).toList();
        }

        if(numberOfSeats != null && numberOfSeats != 0){
            found = found.stream().filter(offer -> offer.getCar().getNumberOfSeats() >= numberOfSeats).toList();
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

            Offer newOffer = offerService.create(offerDTO.getStartDateTime(), offerDTO.getEndDateTime(), offerDTO.getPickupLocation(), car);
            if (newOffer != null) {
                return new ResponseEntity<>(newOffer, HttpStatus.CREATED);
            }

            return ResponseHandler.generateResponse("Offer could not be created", HttpStatus.BAD_REQUEST, null);

        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }


    @Operation(summary = "Update an offer by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Offer updated", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Offer.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
        @ApiResponse(responseCode = "404", description = "Offer not found", content = @Content) })
    @PutMapping("/v1/offers/{id}")
    ResponseEntity<Offer> update(@RequestBody Offer newOffer, @PathVariable Long id) {
        Optional<Offer> optionalOffer = offerService.getSingleById(id);

        if (optionalOffer.isPresent()) {

            Offer offer = optionalOffer.get();

            offer.setStartDateTime(newOffer.getStartDateTime());
            offer.setEndDateTime(newOffer.getEndDateTime());
            offer.setPickupLocation(newOffer.getPickupLocation());
            offer.setCar(newOffer.getCar());

            return ResponseEntity.ok(offerService.save(offer));
        } else {
            return ResponseEntity.notFound().build();
        }
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
