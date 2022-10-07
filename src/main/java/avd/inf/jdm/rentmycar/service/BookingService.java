package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.Booking;
import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.domain.User;
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

    public Booking save(Booking booking) {
        return bookingRepository.save(booking);
    }

}
