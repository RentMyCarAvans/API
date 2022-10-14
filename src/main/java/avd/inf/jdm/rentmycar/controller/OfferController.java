package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.OfferDTO;
import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.service.CarService;
import avd.inf.jdm.rentmycar.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class OfferController {

    private final OfferService offerService;
    private final CarService carService;
    @Autowired
    public OfferController(OfferService offerService, CarService carService) {
        this.offerService = offerService;
        this.carService = carService;
    }

    @GetMapping("/v1/offers")
    public ResponseEntity<List<Offer>> getAllOffers(@RequestParam(required = false) String city){
        List<Offer> found = city == null ? offerService.getAll() : offerService.getOffersByPickupLocation(city);
        return found.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(found);
    }

    @GetMapping("/v1/offers/{id}")
    public ResponseEntity<Optional<Offer>> getById(@PathVariable Long id){
        Optional<Offer> found =  offerService.getSingleById(id);
        return found.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(found);
    }

    // TODO RS: Check if this works after inplementing the bookings-subsystem
    @GetMapping("/v1/offers/unbooked")
    public ResponseEntity<List<Offer>> getAllUnbookedOffers() {
        try {
            List<Offer> found = new ArrayList<>();
            found.addAll(offerService.getUnbooked());
            return found.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(found);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//    @PostMapping("/v1/offers")
//    public ResponseEntity<Object> create(@RequestBody Offer newOffer){
//        try {
//            Offer offer = offerService.create(newOffer);
//            return new ResponseEntity<>(offer, HttpStatus.CREATED);
//        } catch (IllegalArgumentException e) {
//            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
////            return ResponseEntity.badRequest().build();
//        }
//    }

    @PostMapping("/v1/offers")
    public ResponseEntity<Object> create(@RequestBody OfferDTO offerDTO) {
        Car car = carService.getSingleById(offerDTO.getCarId()).get();
        Offer newOffer = offerService.create(offerDTO.getStartDateTime(), offerDTO.getEndDateTime(), offerDTO.getPickupLocation(), car);

        if (newOffer != null) {
            return new ResponseEntity<>(newOffer, HttpStatus.CREATED);
        }

        return ResponseHandler.generateResponse("Offer could not be created", HttpStatus.BAD_REQUEST, null);
        }


    @PutMapping("/v1/offers/{id}")
    ResponseEntity<Offer> updateOffer(@RequestBody Offer newOffer, @PathVariable Long id) {
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

}
