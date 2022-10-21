package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.Booking;
import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    ObjectMapper mapper;
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User saveDTO(String firstName, String lastName, LocalDate dateOfBirth, String email, String password) {
        Optional<User> maybeUser = userRepository.findUserByEmail(email);

        if(maybeUser.isPresent()) {

//            user/email is found, update the user. First map the maybeUser to
            final User mappedUser = mapper.convertValue(maybeUser, User.class);

            userRepository.save(mappedUser);
            return mappedUser;
        } else {
//            user is new, create new one
            User newUser = new User(firstName, lastName, password, dateOfBirth, email, 100);
            userRepository.save(newUser);
            return newUser;

        }
    }

    public Optional<User> getUserByID(Long id)  {
        return userRepository.findById(id);
    }


    public void delete (User user) {
        userRepository.delete(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
