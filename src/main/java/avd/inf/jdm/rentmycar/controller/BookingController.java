package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.Booking;
import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/v1/bookings")
    public ResponseEntity<List<Booking>> getAllBookings(){
        List<Booking> found =  bookingService.getAll();
        return found.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(found);
    }

    @GetMapping("/v1/bookings/{id}")
    public ResponseEntity<Optional<Booking>> getById(@PathVariable Long id){
        Optional<Booking> found =  bookingService.getSingleById(id);
        return found.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(found);
    }

    @PostMapping("/v1/bookings")
    public ResponseEntity<Booking> create(@RequestBody Booking newBooking){
        try {
            Booking booking = bookingService.save(newBooking);
            return new ResponseEntity<>(booking, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/v1/bookings/{id}")
    ResponseEntity<Booking> updateBooking(@RequestBody Booking newBooking, @PathVariable Long id) {
        Optional<Booking> optionalBooking = bookingService.getSingleById(id);

        if (optionalBooking.isPresent()) {

            Booking booking = optionalBooking.get();

            booking.setCustomer(newBooking.getCustomer());
            booking.setOffer(newBooking.getOffer());
            booking.setDropOfLocation(newBooking.getDropOfLocation());

            return ResponseEntity.ok(bookingService.save(booking));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

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