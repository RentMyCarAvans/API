package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }


    public User getUserByID(Long id)  {
        return userRepository.findById(id).get();
    }
}
