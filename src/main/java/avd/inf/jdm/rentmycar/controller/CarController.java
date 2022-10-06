package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import javax.validation.Valid;

@RestController
@RequestMapping(path = "/api")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/v1/cars")
    public ResponseEntity<List<Car>> getAllCars(){
        try {
            List<Car> list = carService.getCars();
            if(list.isEmpty() || list.size() == 0){
                return new ResponseEntity<List<Car>>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<List<Car>>(list, HttpStatus.OK);
        } catch (Exception e) {
            // Internal error
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/v1/car")
    public Car createCar(@Valid @RequestBody Car car) {
        return carService.createCar(car);}


}
