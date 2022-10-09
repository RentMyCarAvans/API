package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    // API for retrieving all cars
    @GetMapping("/v1/cars")
    public ResponseEntity<List<Car>> getAllCars(){
        try {
            List<Car> list = carService.getAllCars();
            if(list.isEmpty() || list.size() == 0){
                return new ResponseEntity<List<Car>>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<List<Car>>(list, HttpStatus.OK);
        } catch (Exception e) {
            // Internal error
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API for adding a new car
    @PostMapping("/v1/cars")
    public Car createCar(@Valid @RequestBody Car car) {
        return carService.createCar(car);
    }

    // API for retrieving a car by id
    @GetMapping("/v1/cars/{id}")
    public ResponseEntity<Optional<Car>> getCarById(@PathVariable Long id){
        Optional<Car> found = carService.getSingleById(id);
        if (found.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(found);
    }

    //API for deleting a car by id
    @DeleteMapping("/v1/cars/{id}")
    public ResponseEntity<HttpStatus> deleteCarById(@PathVariable Long id){
        if (!carService.existCarById(id)){
            return ResponseEntity.notFound().build();
        }
        carService.deleteCarById(id);
        return ResponseEntity.ok().build();
    }

    // API for updating a car
    @PutMapping("/v1/cars/{id}")
    ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car carNewValues){
        Optional<Car> optionalCar = carService.getSingleById(id);

        if(optionalCar.isPresent()){
            Car car = optionalCar.get();
            car.setColorType(carNewValues.getColorType());
            car.setModel(carNewValues.getModel());
            car.setMileage(carNewValues.getMileage());
            car.setLicensePlate(carNewValues.getLicensePlate());
            car.setYearOfManufacture(carNewValues.getYearOfManufacture());

            return ResponseEntity.ok(carService.save(car));
        }
        return ResponseEntity.notFound().build();
    }

}
