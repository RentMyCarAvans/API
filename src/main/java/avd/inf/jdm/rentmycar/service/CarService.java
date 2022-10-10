package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.repository.CarRepository;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class CarService {
    private static final Logger log = LoggerFactory.getLogger((CarService.class));
    @Autowired
    private final CarRepository carRepository;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id).get();
    }

    public Optional<Car> getSingleById(Long id) {
        return carRepository.findById(id);
    }

    public Boolean existCarById(Long id){
        return carRepository.existsById(id);
    }

    public Car createCar(Car car) {
        if (car.getYearOfManufacture() < 2000)
            throw new IllegalArgumentException("Year of manufacture cannot be older than 5 years");
        return carRepository.save(car);
    }

    public Car save(Car car) {
        if (!isValidLicensePlate(car.getLicensePlate())) {
            throw new IllegalArgumentException("Licenseplate " + car.getLicensePlate() + " is invalid");
        }
        return carRepository.save(car);
    }

    public void deleteCarById(Long Id){ carRepository.deleteById(Id);}

    public Boolean isValidLicensePlate(String licensePlate){
        List<String> listOfLicensePlatePatterns = new ArrayList<>();

        // patterns of Dutch licenseplate
        // source: https://www.rdw.nl/particulier/voertuigen/auto/de-kentekenplaat/cijfers-en-letters-op-de-kentekenplaat
        listOfLicensePlatePatterns.add("^([A-Z]{2})([A-Z]{2})(\\d{2})$"); // XX-XX-99
        listOfLicensePlatePatterns.add("^(\\d{2})([A-Z]{2})([A-Z]{2})$"); // 99-XX-XX
        listOfLicensePlatePatterns.add("^(\\d{2})([A-Z]{3})(\\d{1})$");   // 99-XXX-9
        listOfLicensePlatePatterns.add("^(\\d{1})([A-Z]{3})(\\d{2})$");   // 9-XXX-99
        listOfLicensePlatePatterns.add("^([A-Z]{2})(\\d{3})([A-Z]{1})$"); // XX-999-X
        listOfLicensePlatePatterns.add("^([A-Z]{1})(\\d{3})([A-Z]{2})$"); // X-999-XX
        listOfLicensePlatePatterns.add("^([A-Z]{3})(\\d{2})([A-Z]{1})$"); // XXX-99-X
        listOfLicensePlatePatterns.add("^([A-Z]{1})(\\d{2})([A-Z]{3})$"); // X-99-XXX
        listOfLicensePlatePatterns.add("^(\\d{1})([A-Z]{2})(\\d{3})$");   // 9-XX-999
        listOfLicensePlatePatterns.add("^(\\d{3})([A-Z]{2})(\\d{1})$");   // 999-XX-9
        listOfLicensePlatePatterns.add("^(\\d{3})(\\d{2})([A-Z]{1})$");   // 999-99-X
        listOfLicensePlatePatterns.add("^([A-Z]{3})(\\d{2})(\\d{1})$");   // XXX-99-9
        listOfLicensePlatePatterns.add("^([A-Z]{3})([A-Z]{2})(\\d{1})$"); // XXX-XX-9
        for (String pattern : listOfLicensePlatePatterns) {
            if (Pattern.matches(pattern, licensePlate)){
                log.info("[CarService] Car with licenseplate " + licensePlate + " has a match on pattern " + pattern);
                return true;
            };
        }
        log.warn("[CarService] Car with licenseplate " + licensePlate + " has no match with a pattern ");
        return false;
    }

}
