package avd.inf.jdm.rentmycar.controller.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Getter
@Setter
public class AuthDTO {
    @Email
    private String email;
    private String password;
}

