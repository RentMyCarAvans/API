package avd.inf.jdm.rentmycar.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User extends Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private LocalDate dateOfBirth;
    private String email;
    private int bonusPoints;

}
