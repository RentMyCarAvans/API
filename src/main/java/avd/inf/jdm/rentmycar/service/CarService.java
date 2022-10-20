package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.*;
import avd.inf.jdm.rentmycar.repository.CarRepository;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<Car> getAll() {
        return carRepository.findAll();
    }

    public Car getById(Long id) {
        return carRepository.findById(id).get();
    }

    public Optional<Car> getSingleById(Long id) {
        return carRepository.findById(id);
    }

    public Boolean existsById(Long id){
        return carRepository.existsById(id);
    }

    public Boolean existsByLicensePlate(String licensePlate) { return carRepository.existsCarByLicensePlate(licensePlate);}

    public Optional<Car> getByLicensePlate(String licensePlate) {
        log.info("[CarService] getCarByLicensePlate with licenseplate " + licensePlate + " =>" + carRepository.findByLicensePlate(licensePlate));
        return carRepository.findByLicensePlate(licensePlate);
    }

    public Car save(Car car) {
        log.info("[CarService] save()");
        return carRepository.save(car);
    }

    public void deleteById(Long Id){ carRepository.deleteById(Id);}

    public Boolean isValidLicensePlate(String licensePlate){
        log.info("[CarService] isValidLicensePlate()");
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

    public Car create(String type, String licensePlate, Short yearOfManufacture, String model, ColorType colorType, int mileage, int numberOfSeats, User user){
        log.debug("[CarService] createCar(" + licensePlate + "," + yearOfManufacture + "," + model + "," + colorType.name() + mileage + "," + numberOfSeats + ")");
        if (licensePlate == null || licensePlate.isEmpty()) {
            throw new IllegalArgumentException("Licenseplate must not be empty");
        }

        if (user == null) {
            throw new IllegalArgumentException("User must not be empty");
        }

        // Check if a given licenseplate matches a pattern
        if (!isValidLicensePlate(licensePlate)) {
            log.info("[CarService] isValidLicensePlate()" );
            throw new IllegalArgumentException("Licenseplate " + licensePlate + " is invalid");
        }

        // If the licenseplate already exists, then throw a exception
        if (existsByLicensePlate(licensePlate)) {
            log.info("[CarService] existsByLicensePlate() => " + !existsByLicensePlate(licensePlate));
            throw new IllegalArgumentException("Licenseplate " + licensePlate + " is already registered");
        }

        // type determines what concrete class should be used
        Car car = null;
        switch (type){
            case "ICE": car = new ICE();
                break;
            case "BEV": car = new BEV();
                break;
            case "FCEV": car = new FCEV();
                break;
            default: throw new IllegalArgumentException("Cartype " + type + " is not valid. Please provide a valid type");
        }
        car.setLicensePlate(licensePlate);
        car.setYearOfManufacture(yearOfManufacture);
        car.setModel(model);
        car.setColorType(colorType);
        car.setMileage(mileage);
        car.setNumberOfSeats(numberOfSeats);
        car.setUser(user);
        log.info("[CarService] let's save the car" );
        carRepository.save(car);
        return car;
    }

}
