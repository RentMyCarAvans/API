package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.Ride;
import avd.inf.jdm.rentmycar.repository.RideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RideService {
    private final RideRepository rideRepository;
    @Autowired
    public RideService(RideRepository rideRepository) { this.rideRepository = rideRepository;}

    public Ride save(Ride ride) {return rideRepository.save(ride);}

    public Optional<Ride> getRideByID(Long id) { return rideRepository.findById(id);}

    public List<Ride> getAllRides() { return rideRepository.findAll();}

    public List<Ride> getRidesByCarId(Long carId) {
     return rideRepository.findRidesByCarId(carId);
    }
}
