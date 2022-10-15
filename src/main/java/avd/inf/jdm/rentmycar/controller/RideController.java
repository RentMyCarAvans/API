package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.Offer;
import avd.inf.jdm.rentmycar.domain.Ride;
import avd.inf.jdm.rentmycar.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RideController {

private final RideService rideService;

@Autowired
    public RideController(RideService rideService) { this.rideService = rideService;}

    @GetMapping("/v1/rides")
    public ResponseEntity<List<Ride>> getAllRides() {
    List<Ride> found = rideService.getAllRides();
    return found.isEmpty() ?  ResponseEntity.noContent().build() : ResponseEntity.ok(found);
    }
    @GetMapping("/v1/rides/car/{carId}")
    public ResponseEntity<List<Ride>> getRidesById(@PathVariable Long carId) {
        try {
            List<Ride> found = new ArrayList<>();
            found.addAll(rideService.getRidesByCarId(carId));
            return found.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(found);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
@PostMapping
public ResponseEntity<Ride> create(@RequestBody Ride newRide) {
    try {
        Ride ride = rideService.save(newRide);
        return new ResponseEntity<>(ride, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
    @PutMapping("v1/rides/{id}")
    public ResponseEntity<Ride> updateRideByID(@RequestBody Ride newRide, @PathVariable Long id) {
        Optional<Ride> optionalRide = rideService.getRideByID(id);
        if(optionalRide.isPresent()) {
            Ride ride = optionalRide.get();

            ride.setEndRideLatitude(newRide.getEndRideLatitude());
            ride.setEndRideLongitude(newRide.getEndRideLongitude());
            ride.setStartRideLatitude(newRide.getStartRideLatitude());
            ride.setStartRideLongitude(newRide.getStartRideLongitude());
            ride.setMaxAccelerationForce(newRide.getMaxAccelerationForce());
            ride.setTotalKilometersDriven(newRide.getTotalKilometersDriven());
            return ResponseEntity.ok(rideService.save(ride));

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
