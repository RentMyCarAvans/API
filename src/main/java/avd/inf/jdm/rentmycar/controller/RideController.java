package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.Ride;
import avd.inf.jdm.rentmycar.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class RideController {

private final RideService rideService;

@Autowired
    public RideController(RideService rideService) { this.rideService = rideService;}

    @GetMapping("/v1/rides/")
    public ResponseEntity<List<Ride>> getAllRides() {
    List<Ride> found = rideService.getAllRides();
    return found.isEmpty() ?  ResponseEntity.noContent().build() : ResponseEntity.ok(found);
    }
    @GetMapping("/v1/rides/{carId}")
    public ResponseEntity<Ride> getById(@PathVariable Long carId) {
        List<Ride> found = rideService.getRidesByCarId(carId);
        return found.isEmpty() ?  ResponseEntity.noContent().build() : ResponseEntity.ok().build();
    }
}
