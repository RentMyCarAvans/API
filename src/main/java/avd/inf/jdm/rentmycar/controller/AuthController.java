package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.AuthDTO;
import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "Authentication Controller", description = "Endpoints regarding user authentication")
@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Login to Rent My Car")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully logged in",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthDTO.class)) }),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content) })
    @PostMapping("/v1/auth/login")
    public ResponseEntity<Object> login(
            @RequestBody(required = true) AuthDTO authDTO
    ){
        User user = userService.getUserByEmail(authDTO.getEmail());
        if(user == null || !user.getPassword().equals(authDTO.getPassword())) {
            return ResponseHandler.generateResponse("Username or password is incorrect", HttpStatus.UNAUTHORIZED, null);
        }

        // TODO: Create class for creating JWTs
        // For now return a random string
        String randomExampleToken = RandomStringUtils.randomAlphanumeric(50);

        return ResponseHandler.generateResponse("Login successfully", HttpStatus.OK, randomExampleToken);
    }

    @Operation(summary = "Reset password for Rent My Car")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully reset password",content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "User Not Found", content = @Content) })
    @PostMapping("/v1/auth/reset")
    public ResponseEntity<Object> resetPassword(   @RequestBody(required = false) AuthDTO authDTO){
        User user = userService.getUserByEmail(authDTO.getEmail());
        if(user == null) {
            return ResponseHandler.generateResponse("User not found", HttpStatus.NOT_FOUND, null);
        }
        else {
            // TODO: generate and persist new JWT token, implement email functionality, send email with token in link to reset password.
            return ResponseHandler.generateResponse("Password reset email sent to "+ user.getEmail(), HttpStatus.OK, null);
        }
    }


}
