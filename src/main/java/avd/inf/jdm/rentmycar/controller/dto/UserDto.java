package avd.inf.jdm.rentmycar.controller.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
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
    @NotNull
    private String email;

    @NotNull
    private String password;
}
