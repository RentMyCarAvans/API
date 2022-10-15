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
    public ResponseEntity<Object> getAllOffers(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String colorOfCar
    ){

        List<Offer> found = new ArrayList<>();

        found = offerService.getAll();

        if(city != null && !city.isEmpty()){
            found = found.stream().filter(offer -> offer.getPickupLocation().equals(city)).toList();
        }

        if(colorOfCar != null && !colorOfCar.isEmpty()){
            found = found.stream().filter(offer -> offer.getCar().getColorType().equals(colorOfCar)).toList();
        }

        return found.isEmpty()
                ? ResponseHandler.generateResponse("No offers found", HttpStatus.NO_CONTENT, null)
                : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @GetMapping("/v1/offers/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id){
        Optional<Offer> found =  offerService.getSingleById(id);
        return found.isEmpty()
                ? ResponseHandler.generateResponse("Offer with id " + id + " not found", HttpStatus.NOT_FOUND, null)
                : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @GetMapping("/v1/offers/unbooked")
    public ResponseEntity<Object> getAllUnbookedOffers() {
        try {
            List<Offer> found = new ArrayList<>();
            found.addAll(offerService.getUnbooked());
            return found.isEmpty()
                    ? ResponseHandler.generateResponse("No offers found", HttpStatus.NO_CONTENT, null)
                    : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @PostMapping("/v1/offers")
    public ResponseEntity<Object> create(@RequestBody OfferDTO offerDTO) {
        try {
            Car car = carService.getSingleById(offerDTO.getCarId()).get();
            Offer newOffer = offerService.create(offerDTO.getStartDateTime(), offerDTO.getEndDateTime(), offerDTO.getPickupLocation(), car);

            if (newOffer != null) {
                return new ResponseEntity<>(newOffer, HttpStatus.CREATED);
            }

            return ResponseHandler.generateResponse("Offer could not be created", HttpStatus.BAD_REQUEST, null);

        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
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

    @DeleteMapping("/v1/offers/{id}")
    public ResponseEntity<Offer> delete(@PathVariable Long id) {
        Optional<Offer> optionalOffer = offerService.getSingleById(id);

        if (optionalOffer.isPresent()) {
            offerService.delete(optionalOffer.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
