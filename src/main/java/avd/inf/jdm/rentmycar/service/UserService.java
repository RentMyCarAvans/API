package avd.inf.jdm.rentmycar.service;

import avd.inf.jdm.rentmycar.controller.dto.UserDto;
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
import java.util.*;

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

    // got idea from ; https://www.geeksforgeeks.org/spring-boot-crud-operations/
    public User updateUser(UserDto userDto, Long id) {
        Optional<User> maybeUser = userRepository.findById(id);

        if(maybeUser.isPresent()) {
//            user/email is found, update the user.
            if (Objects.nonNull(userDto.getFirstName())
                    && !"".equalsIgnoreCase(
                    userDto.getFirstName())) {
                maybeUser.get().setFirstName(userDto.getFirstName());}

            if (Objects.nonNull(userDto.getLastName())
                    && !"".equalsIgnoreCase(
                    userDto.getLastName())) {
                maybeUser.get().setLastName(userDto.getLastName());}

            if (Objects.nonNull(userDto.getEmail())
                    && !"".equalsIgnoreCase(
                    userDto.getEmail())) {
                maybeUser.get().setEmail(userDto.getEmail());}

              if (Objects.nonNull(userDto.getAddress())
                    && !"".equalsIgnoreCase(
                    userDto.getAddress())) {
                maybeUser.get().setAddress(userDto.getAddress());}

            if (Objects.nonNull(userDto.getCity())
                    && !"".equalsIgnoreCase(
                    userDto.getCity())) {
                maybeUser.get().setCity(userDto.getCity());}

            if (Objects.nonNull(userDto.getTelephone())
                    && !"".equalsIgnoreCase(
                    userDto.getTelephone())) {
                maybeUser.get().setTelephone(userDto.getTelephone());}

            if (Objects.nonNull(userDto.getIsVerifiedUser())) {
                maybeUser.get().setIsVerifiedUser(userDto.getIsVerifiedUser());}

            if (Objects.nonNull(userDto.getDateOfBirth())) {
                maybeUser.get().setDateOfBirth(userDto.getDateOfBirth());}

            if (Objects.nonNull(userDto.getPassword())
                    && !"".equalsIgnoreCase(
                    userDto.getPassword())) {
                maybeUser.get().setPassword(userDto.getPassword());}

            maybeUser.get().setBonusPoints(userDto.getBonusPoints());

            userRepository.save(maybeUser.get());
            return maybeUser.get();
        } else {
//            cannot find user
            throw new IllegalArgumentException("Cannot find user");
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
