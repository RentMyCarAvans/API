package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {
    private final OfferRepository offerRepository;
    @Autowired
    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public List<Offer> getAll() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getSingleById(Long id) {
        // Check if the input is valid
        if (id == null) {
            throw new IllegalArgumentException("The id must not be null");
        }

        // Check if the offer exists
        if (offerRepository.existsById(id)) {
            return offerRepository.findById(id);
        } else {
            return Optional.empty();
        }
    }

    public List<Offer> getOffersByPickupLocation(String city) {
        // Check if the input is valid
        if (city == null || city.isEmpty()) {
            throw new IllegalArgumentException("The city must not be empty");
        }
        return offerRepository.findOffersByPickupLocation(city);
    }

    public List<Offer> getUnbooked() {
        return offerRepository.findUnbooked();
    }

    public Offer create(Offer offer) {
        // Check if the input is valid
        if (offer == null) {
            throw new IllegalArgumentException("The offer must not be null");
        }

        if (offer.getPickupLocation() == null || offer.getPickupLocation().isEmpty()) {
            throw new IllegalArgumentException("Pickup location must not be empty");
        }

        if (offer.getStartDateTime() == null) {
            throw new IllegalArgumentException("Start date must not be empty");
        }

        if(offer.getStartDateTime().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Start date must not be in the past");
        }

        if (offer.getStartDateTime().isEqual(offer.getEndDateTime())) {
            throw new IllegalArgumentException("Start date must not be equal to end date");
        }

        if (offer.getEndDateTime() == null) {
            throw new IllegalArgumentException("End date must not be empty");
        }

        if(offer.getEndDateTime().isBefore(offer.getStartDateTime())) {
            throw new IllegalArgumentException("End date must not be before start date");
        }

        return offerRepository.save(offer);
    }

    // TODO RS: Create and save are the same, maybe refactor this
    public Offer save(Offer offer) {
        // Check if the input is valid
        if (offer == null) {
            throw new IllegalArgumentException("The offer must not be null");
        }

        if (offer.getPickupLocation() == null || offer.getPickupLocation().isEmpty()) {
            throw new IllegalArgumentException("Pickup location must not be empty");
        }

        if (offer.getStartDateTime() == null) {
            throw new IllegalArgumentException("Start date must not be empty");
        }

        if(offer.getStartDateTime().isBefore(java.time.LocalDateTime.now())) {
            throw new IllegalArgumentException("Start date must not be in the past");
        }

        if (offer.getStartDateTime().isEqual(offer.getEndDateTime())) {
            throw new IllegalArgumentException("Start date must not be equal to end date");
        }

        if (offer.getEndDateTime() == null) {
            throw new IllegalArgumentException("End date must not be empty");
        }

        if(offer.getEndDateTime().isBefore(offer.getStartDateTime())) {
            throw new IllegalArgumentException("End date must not be before start date");
        }

        return offerRepository.save(offer);
    }
}
