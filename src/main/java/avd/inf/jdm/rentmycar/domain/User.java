package avd.inf.jdm.rentmycar.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Users")
@DiscriminatorValue("1")

public class User extends Account {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int bonusPoints;


    public User(String firstName, String lastName, String password, LocalDate dateOfBirth, String email, int bonusPoints) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bonusPoints = bonusPoints;
    }
}
