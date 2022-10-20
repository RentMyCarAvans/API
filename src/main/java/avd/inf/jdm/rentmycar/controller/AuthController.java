package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.ResponseHandler;
import avd.inf.jdm.rentmycar.controller.dto.AuthDTO;
import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Tag(name = "auth-controller", description = "Endpoints regarding user authentication")
@RestController
@RequestMapping("/api")
@CrossOrigin
public class AuthController {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

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


}
