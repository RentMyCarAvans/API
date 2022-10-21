package avd.inf.jdm.rentmycar.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
@Getter
@Setter
public class UserDto {
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private LocalDate dateOfBirth;
    @Email
    @NotNull
    private String email;

    @NotEmpty
    @Size(min = 6, message = "password should have at least 6 characters")
    private String password;
}
