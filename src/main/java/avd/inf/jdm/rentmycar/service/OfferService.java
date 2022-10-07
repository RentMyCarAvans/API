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
        return offerRepository.findById(id);
    }

    public List<Offer> getOffersByPickupLocation(String city) {
        return offerRepository.findOffersByPickupLocation(city);
    }

    public List<Offer> getUnbooked() {
        return offerRepository.findUnbooked();
    }

    public Offer create(Offer offer) {
        return offerRepository.save(offer);
    }

    public Offer save(Offer offer) {
        return offerRepository.save(offer);
    }
}
