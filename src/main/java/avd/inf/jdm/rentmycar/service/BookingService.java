package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.*;
import avd.inf.jdm.rentmycar.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAll() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getSingleById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> getBookingsByCustomer(User customer) {
        return bookingRepository.findBookingsByCustomer(customer);
    }

    public Optional<Booking> getBookingByOffer(Offer offer) {
        return bookingRepository.findBookingByOffer(offer);
    }

    public Optional<List<Booking>> getBookingByStatus(BookingStatus status) {
        return bookingRepository.findBookingByStatus(status);
    }

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking startRide(Booking booking) {
        Ride newRide = new Ride(booking);
        booking.setRide(newRide);
        booking.setStatus(BookingStatus.PICKEDUP);

        return bookingRepository.save(booking);

    }

    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }
}