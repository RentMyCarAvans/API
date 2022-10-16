package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.BookingDTO;
import avd.inf.jdm.rentmycar.domain.Booking;
import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.service.BookingService;
import avd.inf.jdm.rentmycar.service.OfferService;
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

    @Autowired
    public BookingController(BookingService bookingService, OfferService offerService, UserService userService) {
        this.bookingService = bookingService;
        this.offerService = offerService;
        this.userService = userService;
    }

    @Operation(summary = "Retrieving all bookings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all bookings",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Booking.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No bookings found",content = @Content) })
    @GetMapping("/v1/bookings")
    public ResponseEntity<Object> getAllBookings(){
        List<Booking> found =  bookingService.getAll();
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
            Offer offer = offerService.getSingleById(bookingDTO.getOfferId()).get();
            User customer = userService.getUserByID(bookingDTO.getCustomerId());
            Booking newBooking = bookingService.create(offer, customer);

            if (newBooking != null) {
                return new ResponseHandler().generateResponse("Booking created", HttpStatus.CREATED, newBooking);
            }

            return new ResponseHandler().generateResponse("Booking could not be created", HttpStatus.BAD_REQUEST, null);

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
    ResponseEntity<Booking> updateBooking(@RequestBody Booking newBooking, @PathVariable Long id) {
        Optional<Booking> optionalBooking = bookingService.getSingleById(id);

        if (optionalBooking.isPresent()) {

            Booking booking = optionalBooking.get();

            booking.setCustomer(newBooking.getCustomer());
            booking.setOffer(newBooking.getOffer());
            booking.setDropOfLocation(newBooking.getDropOfLocation());
            booking.setStatus(newBooking.getStatus());

            return ResponseEntity.ok(bookingService.save(booking));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a booking by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking deleted",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = Booking.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",content = @Content),
            @ApiResponse(responseCode = "404", description = "Booking not found",content = @Content) })
    @DeleteMapping("/v1/bookings/{id}")
    public ResponseEntity<Booking> delete(@PathVariable Long id) {
        Optional<Booking> optionalBooking = bookingService.getSingleById(id);

        if (optionalBooking.isPresent()) {
            bookingService.delete(optionalBooking.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}