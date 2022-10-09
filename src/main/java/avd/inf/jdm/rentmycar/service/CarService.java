package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.repository.CarRepository;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {
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
        return carRepository.save(car);
    }

    public void deleteCarById(Long Id){ carRepository.deleteById(Id);}

    public Boolean isValidLicensePlate(Car car){
        return true;
    }

}
