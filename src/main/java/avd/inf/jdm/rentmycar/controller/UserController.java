package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/users")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/user")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/user")
    public User createUser(@Valid @RequestBody User user) { return userService.createUser(user);}


    @GetMapping("/user/{id}")
    public User getUserByID(@PathVariable Long id)  { return userService.getUserByID(id);  }


}
