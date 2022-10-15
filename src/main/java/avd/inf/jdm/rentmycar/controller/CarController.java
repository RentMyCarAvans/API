package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.service.CarService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
@OpenAPIDefinition(
        info = @Info(
                title = "Rent My Car Avans",
                description = "" +
                        "Overview of available API's"))
@Tag(name = "car-controller", description = "API's to add new cars, delete a car, retrieve all cars, retrieve specific car information")
@RestController
@RequestMapping(path = "/api")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @Operation(summary = "Retrieving all cars")
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

    @Operation(summary = "Add a new car")
    @PostMapping("/v1/cars")
    public Car createCar(@Valid @RequestBody Car car) {
        return carService.createCar(car);
    }

    @Operation(summary = "Retrieve a car by id")
    @GetMapping("/v1/cars/{id}")
    public ResponseEntity<Optional<Car>> getCarById(@PathVariable Long id){
        Optional<Car> found = carService.getSingleById(id);
        if (found.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(found);
    }

    @Operation(summary = "Delete a car by id")
    @DeleteMapping("/v1/cars/{id}")
    public ResponseEntity<HttpStatus> deleteCarById(@PathVariable Long id){
        if (!carService.existCarById(id)){
            return ResponseEntity.notFound().build();
        }
        carService.deleteCarById(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update a car by id")
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
