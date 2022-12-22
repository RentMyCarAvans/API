package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    public User saveDTO(String firstName, String lastName, LocalDate dateOfBirth, String email, String password,String address, String city, Boolean isVerifiedUser, int bonusPoints, String telephone) {
        Optional<User> maybeUser = userRepository.findUserByEmail(email);

        if(maybeUser.isPresent()) {

//            user/email is found, update the user. First map the maybeUser to
            final User mappedUser = mapper.convertValue(maybeUser, User.class);
            mappedUser.setFirstName(firstName);
            mappedUser.setLastName(lastName);
            mappedUser.setDateOfBirth(dateOfBirth);
            mappedUser.setBonusPoints(bonusPoints);
            mappedUser.setAddress(address);
            mappedUser.setCity(city);
            mappedUser.setTelephone(telephone);
            mappedUser.setIsVerifiedUser(isVerifiedUser);
            mappedUser.setEmail(email);
            userRepository.save(mappedUser);
            return mappedUser;
        } else {
//            user is new, create new one
            User newUser = new User(firstName, lastName, password, dateOfBirth, email, 100, address, city, telephone, isVerifiedUser);
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



    public Map setProfilePicture (MultipartFile file, Long id){
        // first find user
        Optional<User> maybeUser = userRepository.findById(id);
        // if no user is found, return early.
        if(maybeUser.isEmpty()) return null;

        String ImagePath = "images";


        File directory = new File(ImagePath);
        if (!directory.exists()) {
            try {
                directory.mkdir();
            } catch (SecurityException se) {
                return null;
            }
        }
        String fileName = System.currentTimeMillis() + file.getOriginalFilename();
        final String path = ImagePath + File.separator + fileName;
        try (InputStream inputStream = file.getInputStream();

             FileOutputStream fileOutputStream = new FileOutputStream(new File(path))) {
            byte[] buf = new byte[1024];
            int numRead = 0;
            while ((numRead = inputStream.read(buf)) >= 0) {
                fileOutputStream.write(buf, 0, numRead);
            }
        } catch (Exception e) {
            return null;
        }
        Map responseResult = new HashMap<>();
        responseResult.put("ProfilePhoto", fileName);

        // save profilepic url to db
        if(maybeUser.isPresent()) {
//            user/email is found, update the user with profilepic
            final User mappedUser = mapper.convertValue(maybeUser, User.class);
                mappedUser.setProfileImageUrl(fileName);
            userRepository.save(mappedUser);
        }

        return responseResult;
    }

    public String getProfilePictureByUserID(Long id) {
      return  userRepository.findById(id).get().getProfileImageUrl();
    }

}
