package avd.inf.jdm.rentmycar.controller;

import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping(path = "api")
public class UserController {
    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/v1/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @PostMapping("/v1/users")
    public User createUser(@Valid @RequestBody User user) { return userService.save(user);}


    @GetMapping("/v1/users/{id}")
    public User getUserByID(@PathVariable Long id)  { return userService.getUserByID(id);  }



}
