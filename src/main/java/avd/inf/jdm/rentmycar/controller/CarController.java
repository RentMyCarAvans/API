package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.CarDTO;
import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.service.CarService;
import avd.inf.jdm.rentmycar.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping(path = "/api")
@CrossOrigin
public class CarController {

    private final CarService carService;
    private final UserService userService;

    @Autowired
    public CarController(CarService carService, UserService userService) {
        this.carService = carService;
        this.userService = userService;
    }

    @Operation(summary = "Retrieving all cars")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all cars",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No cars found",content = @Content) })
    @GetMapping("/v1/cars")
    public ResponseEntity<Object> getAll(){
        log.debug("[CarController] invoke GET api/v1/cars");
        try {
            List<Car> found = carService.getAll();
            return found.isEmpty()
                    ? ResponseHandler.generateResponse("No cars found", HttpStatus.NO_CONTENT, null)
                    : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
        } catch (Exception e) {
            return ResponseHandler.generateResponse("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
    }

    @Operation(summary = "Add a new car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Car created",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No cars found",content = @Content) })
    @PostMapping("/v1/cars")
    public ResponseEntity<Object> create(@RequestBody CarDTO carDTO) {
        log.debug("[CarController] invoke POST api/v1/cars");
        if (!carService.isValidLicensePlate(carDTO.getLicensePlate())){
            log.debug("[CarController] Licenseplate \" + carDTO.getLicensePlate() + \" is invalid");
            return ResponseHandler.generateResponse("Licenseplate " + carDTO.getLicensePlate() + " is invalid. Please enter a valid licenseplate", HttpStatus.BAD_REQUEST, null);
        }
        try {
            User user = userService.getUserByID(carDTO.getUserId()).get();
            Car newCar = carService.create(carDTO.getType(), carDTO.getLicensePlate(), carDTO.getYearOfManufacture(), carDTO.getModel(), carDTO.getColorType(), carDTO.getMileage(), carDTO.getNumberOfSeats(), user);
            if (newCar != null) {
                return new ResponseEntity<>(newCar, HttpStatus.CREATED);
            }

            return ResponseHandler.generateResponse("Car could not be created", HttpStatus.BAD_REQUEST, null);

        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }
    }

    @Operation(summary = "Retrieve a car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car succesfully retrieved",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No car found",content = @Content) })
    @GetMapping("/v1/cars/{id}")
    public ResponseEntity<Object> getById(@PathVariable Long id){
        log.debug("[CarController] invoke GET api/v1/cars/{" + id + "}");
        Optional<Car> found = carService.getSingleById(id);
        return found.isEmpty()
                ? ResponseHandler.generateResponse("Car with id " + id + " not found", HttpStatus.NOT_FOUND, null)
                : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Retrieve a car by licenseplate")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car succesfully retrieved",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No car found",content = @Content) })
    @GetMapping("/v1/cars/query")
    public ResponseEntity<Object> getByLicensePlate(@RequestParam(name = "licenseplate") String licensePlate){
        log.debug("[CarController] invoke GET api/v1/cars/{" + licensePlate + "}");
        Optional<Car> found = carService.getByLicensePlate(licensePlate);
        return found.isEmpty()
                ? ResponseHandler.generateResponse("Car with licenseplante " + licensePlate + " not found", HttpStatus.NOT_FOUND, null)
                : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Delete a car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car succesfully deleted",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No car found",content = @Content) })
    @DeleteMapping("/v1/cars/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id){
        log.debug("[CarController] invoke DELETE api/v1/cars/{" + id + "}");
        if (!carService.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        try {
            carService.deleteById(id);
        }
        catch (Exception e) {
            return ResponseHandler.generateResponse("Car can't be deleted", HttpStatus.INTERNAL_SERVER_ERROR, null);
        }
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update a car by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Car succesfully updated",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Car.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No car found",content = @Content) })
    @PutMapping("/v1/cars/{id}")
    ResponseEntity<Car> update(@PathVariable Long id, @RequestBody Car carNewValues){
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
