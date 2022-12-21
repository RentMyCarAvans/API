package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.UserDto;
import avd.inf.jdm.rentmycar.domain.Image;
import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.repository.ImageRepository;
import avd.inf.jdm.rentmycar.service.ImageService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@OpenAPIDefinition(
        info = @Info(
                title = "Rent My Car Avans",
                description = "" +
                        "Overview of available API's"))
@Tag(name = "user-controller", description = "API's to add new user, delete a user, retrieve all users")
@RestController
@RequestMapping("/api")
@CrossOrigin
public class UserController {
    private final UserService userService;
    private final ImageService imageService;

    @Autowired
    public UserController(UserService userService, ImageService imageService) {
        this.userService = userService;
        this.imageService = imageService;
    }

    @Operation(summary = "Retrieving all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all users", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "No useres found", content = @Content)})
    @GetMapping("/v1/users")
    public ResponseEntity<Object> getAllUsers() {
        List<User> found = userService.getAll();
        return found.isEmpty() ? ResponseHandler.generateResponse("No users found", HttpStatus.NO_CONTENT, null) : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Add a new User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "409", description = "User already exists", content = @Content)})
    @PostMapping("/v1/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserDto userDto) {
        User newUser;
        try {
            Optional<User> maybeUser = userService.getUserByEmail(userDto.getEmail());
            if (maybeUser.isEmpty()) {
                newUser = userService.saveDTO(userDto.getFirstName(), userDto.getLastName(), userDto.getDateOfBirth(), userDto.getEmail(), userDto.getPassword());
                return  ResponseHandler.generateResponse("User created", HttpStatus.CREATED, newUser);

            }
            return ResponseHandler.generateResponse("Email already exists", HttpStatus.BAD_REQUEST, null);


        } catch (IllegalArgumentException e) {
            return ResponseHandler.generateResponse(e.getMessage(), HttpStatus.BAD_REQUEST, null);
        }

    }

    @Operation(summary = "Retrieve a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
    @GetMapping("/v1/users/{id}")
    public ResponseEntity<Object> getUserByID(@PathVariable Long id)  {
        Optional<User> found = userService.getUserByID(id);
        return found.isEmpty()
                ? ResponseHandler.generateResponse("User with id " + id + " not found", HttpStatus.NOT_FOUND, null)
                : ResponseHandler.generateResponse(null, HttpStatus.OK, found);
    }

    @Operation(summary = "Delete a user by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
    @DeleteMapping("/v1/users/{id}")
    public ResponseEntity<Object> delete(@PathVariable Long id) {
        Optional<User> optionalUser = userService.getUserByID(id);

        if (!optionalUser.isPresent()) {
            ResponseHandler.generateResponse("User with id" + id + " not found", HttpStatus.NO_CONTENT, null);
        }

        try {
            userService.delete(optionalUser.get());

        } catch (Exception e) {
            return ResponseHandler.generateResponse("User can't be deleted", HttpStatus.INTERNAL_SERVER_ERROR, null);

        }
        return ResponseHandler.generateResponse("User with id " + id + " is succesfully deleted", HttpStatus.OK, null);
    }

//    @Operation(summary = "Upload profilephoto")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Uploaded profile picture", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
//            @ApiResponse(responseCode = "400", description = "Invalid photo", content = @Content),
//            @ApiResponse(responseCode = "404", description = "User not found", content = @Content)})
//    @PostMapping("/v1/users/profilephoto/{id}")
//    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam("image") MultipartFile file)
//            throws IOException {
//
//        ImageRepository.save(Image.builder()
//                .name(file.getOriginalFilename())
//                .type(file.getContentType())
//                .image(ImageUtility.compressImage(file.getBytes())).build());
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(new ImageUploadResponse("Image uploaded successfully: " +
//                        file.getOriginalFilename()));
//    }
    @PostMapping("/v1/users/profilephoto/{id}")
    public ResponseEntity uploadProfilePhoto(@RequestParam("file") MultipartFile file ) {
        return ResponseHandler.generateResponse( "uploaded image", HttpStatus.OK, imageService.uploadImage(file));
    }

    @GetMapping(path = {"/v1/users/profilephoto/{id}"})
    public ResponseEntity getImage(@PathVariable Long id) throws IOException {

        return ResponseHandler.generateResponse( "uploaded image", HttpStatus.OK, imageService.getPhotoByUserID(id));

    }
}



