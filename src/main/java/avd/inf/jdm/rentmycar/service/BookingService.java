package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.*;
import avd.inf.jdm.rentmycar.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public Booking saveAndFlush(Booking booking) {
        return bookingRepository.saveAndFlush(booking);
    }
    public boolean startRide(Booking booking) {
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found");
        }

            Ride optionalRide = booking.getRide();
            if(optionalRide == null) {
                Ride newRide = new Ride(booking);
                booking.setRide(newRide);


            } else {
//                optionalRide.setStartDateTime(LocalDateTime.now());
                booking.setRide(optionalRide);
            }
            booking.setStatus(BookingStatus.PICKEDUP);

             bookingRepository.save(booking);
             return true;


    }

    public boolean endRide(Booking booking) {
        if(booking.getId() == null ) {
            throw new IllegalArgumentException("Booking must not be null");
        }

        if(bookingRepository.existsById(booking.getId())) {
            booking.setStatus(BookingStatus.RETURNED);

            Ride updatedRide = booking.getRide();
            updatedRide.setEndDateTime(LocalDateTime.now());
            //    calculate the bonuspoints here
            booking.calculateBonusPointsForThisRide();

            bookingRepository.save(booking);
            return true;


        }
        return false;
    }

    public void delete(Booking booking) {
        bookingRepository.delete(booking);
    }

    public Booking create(Offer offer, User customer) {
        Booking booking = new Booking();
        booking.setOffer(offer);
        booking.setCustomer(customer);
        booking.setStatus(BookingStatus.PENDING);
        return bookingRepository.save(booking);
    }


}