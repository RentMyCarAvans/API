package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.BookingDTO;
import avd.inf.jdm.rentmycar.controller.dto.EndRideDTO;
import avd.inf.jdm.rentmycar.domain.Booking;
import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.domain.Ride;
import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.service.BookingService;

import avd.inf.jdm.rentmycar.service.OfferService;
import avd.inf.jdm.rentmycar.service.RideService;
import avd.inf.jdm.rentmycar.service.UserService;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Tag(name = "booking-controller", description = "Endpoints to add new bookings, delete a booking, retrieve all bookings, retrieve specific booking information")

@RestController
@RequestMapping("/api")
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;
    private final OfferService offerService;
    private final UserService userService;
    private final RideService rideService;

    @Autowired
    public BookingController(BookingService bookingService, OfferService offerService, UserService userService, RideService rideService) {
        this.bookingService = bookingService;
        this.offerService = offerService;
        this.userService = userService;
        this.rideService = rideService;
    }

    @Operation(summary = "Retrieving all bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all bookings",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Booking.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No bookings found",content = @Content) })
    @GetMapping("/v1/bookings")
    public ResponseEntity<Object> getAllBookings(
            @RequestParam(required = false) Long customerId
    ){
        List<Booking> found =  bookingService.getAll();

        if(customerId != null) {
            User customer = userService.getUserByID(customerId).orElseThrow(() -> new IllegalArgumentException("Customer not found"));
            found = bookingService.getBookingsByCustomer(customer);
        }



        return found.isEmpty()
                ? ResponseHandler.generateResponse("No bookings found", HttpStatus.NO_CONTENT, null)
                : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Retrieve a booking by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the booking",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Booking.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found",content = @Content) })
    @GetMapping("/v1/bookings/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id){
        Optional<Booking> found =  bookingService.getSingleById(id);
        return found.isEmpty()
                ? ResponseHandler.generateResponse("Booking with id " + id + " not found", HttpStatus.NOT_FOUND, null)
                : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Add a new booking")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Booking created",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Booking.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",content = @Content),
            @ApiResponse(responseCode = "409", description = "Booking already exists",content = @Content) })
    @PostMapping("/v1/bookings")
    public ResponseEntity<Object> create(@RequestBody BookingDTO bookingDTO){
        try {
            Offer offer = offerService.getSingleById(bookingDTO.getOfferId()).isPresent() ? offerService.getSingleById(bookingDTO.getOfferId()).get() : null;
            User customer = userService.getUserByID(bookingDTO.getCustomerId()).isPresent() ? userService.getUserByID(bookingDTO.getCustomerId()).get() : null;

            if (bookingService.getBookingByOffer(offer).isPresent()) {
                return ResponseHandler.generateResponse("Offer " + offer.getId() + " has already been booked.", HttpStatus.CONFLICT, null);
            }

            if (offer.getCar().getUser().getId() == customer.getId()) {
                return ResponseHandler.generateResponse("You cannot book your own car.", HttpStatus.CONFLICT, null);
            }

            if(offer == null || customer == null){
                return ResponseHandler.generateResponse("Offer or customer not found", HttpStatus.NOT_FOUND, null);
            }

            Booking newBooking = bookingService.create(offer, customer);

            if (newBooking != null) {
                return ResponseHandler.generateResponse("Offer with id " + offer.getId() + " has been booked by " + customer.getFirstName() + " " + customer.getLastName(), HttpStatus.CREATED, newBooking);
            }

            return ResponseHandler.generateResponse("Booking could not be created", HttpStatus.BAD_REQUEST, null);

        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @Operation(summary = "Update a booking by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking updated", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Booking.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content) })
    @PutMapping("/v1/bookings/{id}")
    ResponseEntity<Object> updateBooking(@RequestBody Booking newBooking, @PathVariable Long id) {
        Optional<Booking> optionalBooking = bookingService.getSingleById(id);

        if (optionalBooking.isPresent()) {

            Booking booking = optionalBooking.get();

            booking.setCustomer(newBooking.getCustomer());
            booking.setOffer(newBooking.getOffer());
            booking.setDropOfLocation(newBooking.getDropOfLocation());
            booking.setStatus(newBooking.getStatus());

            return ResponseHandler.generateResponse("Booking with id " + id + " is succesfully updated", HttpStatus.OK, bookingService.save(booking));

        }
        return ResponseHandler.generateResponse("Booking with id " + id + " could not be found", HttpStatus.NOT_FOUND, null);
    }

    @Operation(summary = "Delete a booking by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking deleted",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Booking.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found",content = @Content) })
    @DeleteMapping("/v1/bookings/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<Booking> optionalBooking = bookingService.getSingleById(id);

        if (optionalBooking.isPresent()) {
            bookingService.delete(optionalBooking.get());
            return ResponseHandler.generateResponse("Booking with id " + id + " is succesfully deleted", HttpStatus.OK, null);
        } else {
            return ResponseHandler.generateResponse("Booking with id " + id + " could not be found", HttpStatus.NOT_FOUND, null);
        }
    }

    @Operation(summary = "End A Ride")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking and ride updated", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Booking.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found", content = @Content) })
    @PostMapping("/v1/bookings/{bookingId}/endride")
    ResponseEntity<Object> endARide(@RequestBody EndRideDTO endRideDTO, @PathVariable Long bookingId) {
        Optional<Booking> optionalBooking = bookingService.getSingleById(bookingId);

        if (optionalBooking.isPresent()) {

            Booking booking = optionalBooking.get();

            booking.setStatus(endRideDTO.getStatus());
            Ride maybeRide = booking.getRide();
            if (maybeRide != null) {

                maybeRide.setEndDateTime(endRideDTO.getEndDateTime());
                maybeRide.setEndRideLongitude(endRideDTO.getEndRideLongitude());
                maybeRide.setEndRideLatitude(endRideDTO.getEndRideLatitude());
                maybeRide.setStartRideLatitude(endRideDTO.getStartRideLatitude());
                maybeRide.setStartRideLongitude(endRideDTO.getStartRideLongitude());
                maybeRide.setTotalKilometersDriven(endRideDTO.getTotalKilometersDriven());
                maybeRide.setMaxAccelerationForce(endRideDTO.getMaxAccelerationForce());
                rideService.save(maybeRide);
            }
            else {
                Ride newRide = new Ride(booking);
                newRide.setStartDateTime(LocalDateTime.now());
                newRide.setEndDateTime(endRideDTO.getEndDateTime());
                newRide.setEndRideLongitude(endRideDTO.getEndRideLongitude());
                newRide.setEndRideLatitude(endRideDTO.getEndRideLatitude());
                newRide.setStartRideLongitude(endRideDTO.getStartRideLongitude());
                newRide.setStartRideLatitude(endRideDTO.getStartRideLatitude());
                newRide.setTotalKilometersDriven(endRideDTO.getTotalKilometersDriven());
                newRide.setMaxAccelerationForce(endRideDTO.getMaxAccelerationForce());
                booking.setRide(newRide);
            }
            bookingService.save(booking);
            return ResponseHandler.generateResponse("Booking with id " + bookingId + " is succesfully updated", HttpStatus.OK, bookingService.save(booking));
        } else {
            return ResponseHandler.generateResponse("Booking with id " + bookingId + " could not be found", HttpStatus.NOT_FOUND, null);
        }
    }


}