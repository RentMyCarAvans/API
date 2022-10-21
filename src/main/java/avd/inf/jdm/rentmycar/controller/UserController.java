package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.UserDto;
import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

@OpenAPIDefinition(
        info = @Info(
                title = "Rent My Car Avans",
                description = "" +
                        "Overview of available API's"))
@Tag(name = "user-controller", description = "API's to add new user, delete a user, retrieve all users")
@RestController
@RequestMapping(path = "api")
@CrossOrigin
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Retrieving all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all users",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request",content = @Content),
            @ApiResponse(responseCode = "404", description = "No useres found",content = @Content) })
    @GetMapping("/v1/users")
    public ResponseEntity<Object> getAllUsers() {
        List<User> found = userService.getAll();
        return found.isEmpty() ? ResponseHandler.generateResponse("No users found", HttpStatus.NO_CONTENT, null) : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Add a new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists",content = @Content) })
    @PostMapping("/v1/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        User newUser;
        try {
            User maybeUser = userService.getUserByEmail(userDto.getEmail());
            if (maybeUser == null) {
                newUser = userService.saveDTO(userDto.getFirstName(), userDto.getLastName(), userDto.getDateOfBirth(), userDto.getEmail(), userDto.getPassword());
                return new ResponseHandler().generateResponse("User created", HttpStatus.CREATED, newUser);

            }
            return new ResponseHandler().generateResponse("Email already exists", HttpStatus.BAD_REQUEST, null);


    } catch (IllegalArgumentException e) {
        return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
    }

    }

    @Operation(summary = "Retrieve a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",content = @Content) })
    @GetMapping("/v1/users/{id}")
    public ResponseEntity<Object> getUserByID(@PathVariable Long id)  {
        Optional<User> found = userService.getUserByID(id);
        return found.isEmpty() ? ResponseHandler.generateResponse("User with id" + id + " not found", HttpStatus.NO_CONTENT, null)
       : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Delete a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",content = { @Content(mediaType = "application/json",schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",content = @Content) })
    @DeleteMapping("/v1/users/{id}")
    public ResponseEntity<User> delete(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUserByID(id);

        if (optionalUser.isPresent()) {
            userService.delete(optionalUser.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
