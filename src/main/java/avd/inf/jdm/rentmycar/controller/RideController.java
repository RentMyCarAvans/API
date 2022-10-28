package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.domain.Ride;
import avd.inf.jdm.rentmycar.service.RideService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
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
@OpenAPIDefinition(
        info = @Info(
                title = "Rent My Car Avans",
                description = "" +
                        "Overview of available API's"))
@Tag(name = "ride-controller", description = "API's to add new ride, delete a ride, retrieve all rides, get ride by id, update ride by id")
@RestController
@RequestMapping("/api")
public class RideController {

    private final RideService rideService;

    @Autowired
    public RideController(RideService rideService) { this.rideService = rideService;}

    @Operation(summary = "Retrieving all rides")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all rides",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Ride.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No bookings found",content = @Content) })
    @GetMapping("/v1/rides")
    public ResponseEntity<Object> getAllRides() {
        List<Ride> found = rideService.getAllRides();
        return found.isEmpty() ?  ResponseHandler.generateResponse("No bookings found", HttpStatus.NO_CONTENT, null) :ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Retrieve a ride by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the ride",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Ride.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found",content = @Content) })
    @GetMapping("/v1/rides/car/{carId}")
    public ResponseEntity<Object> getRidesById(@PathVariable Long carId) {
        try {
            List<Ride> found = new ArrayList<>();
            found.addAll(rideService.getRidesByCarId(carId));
            return found.isEmpty() ?
                    ResponseHandler.generateResponse("No rides found", HttpStatus.NO_CONTENT, null) : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Invalid id supplied", HttpStatus.BAD_REQUEST, null);
        }

    }

    @Operation(summary = "Add a new Ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ride created",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Ride.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",content = @Content),
            @ApiResponse(responseCode = "409", description = "Ride already exists",content = @Content) })
    @PostMapping
    public ResponseEntity<Object> create(@RequestBody Ride newRide) {
        try {
            Ride ride = rideService.save(newRide);
            return ResponseHandler.generateResponse("Ride created", HttpStatus.CREATED, ride);
        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse("Invalid input", HttpStatus.BAD_REQUEST, null);
        }
    }

    @Operation(summary = "Update a ride by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "ride updated", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Ride.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "ride not found", content = @Content) })
    @PutMapping("v1/rides/{id}")
    public ResponseEntity<Object> updateRideByID(@RequestBody Ride newRide, @PathVariable Long id) {
        Optional<Ride> optionalRide = rideService.getRideByID(id);
        if(optionalRide.isPresent()) {
            Ride ride = optionalRide.get();

            ride.setEndRideLatitude(newRide.getEndRideLatitude());
            ride.setEndRideLongitude(newRide.getEndRideLongitude());
            ride.setStartRideLatitude(newRide.getStartRideLatitude());
            ride.setStartRideLongitude(newRide.getStartRideLongitude());
            ride.setMaxAccelerationForce(newRide.getMaxAccelerationForce());
            ride.setTotalKilometersDriven(newRide.getTotalKilometersDriven());
            return ResponseHandler.generateResponse("Ride with id " + id + " has been updated", HttpStatus.OK, rideService.save(ride));

        } else {
            return ResponseHandler.generateResponse("Ride not found", HttpStatus.NOT_FOUND, null);
        }
    }
}
