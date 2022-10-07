package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public ResponseEntity<List<User>> getUsers() {
        List<User> isFound;
        isFound = userService.getUsers();
        if (isFound.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(isFound);
        }
    }


    @PostMapping()
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try {
            User newUser = userService.createUser(user);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public User getUserByID(@PathVariable Long id) {
        return userService.getUserByID(id);
    }

    @PutMapping("/{id}")
    ResponseEntity<User> updateUser(@RequestBody User newUser, @PathVariable Long id) {
        Optional<User> optionalUser = Optional.ofNullable(userService.getUserByID(id));

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            user.setDateOfBirth(newUser.getDateOfBirth());
            user.setBonusPoints(newUser.getBonusPoints());

            return ResponseEntity.ok(userService.createUser(user));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}