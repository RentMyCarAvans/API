package avd.inf.jdm.rentmycar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("1")
@JsonIgnoreProperties({"hibernateLazyInitializer", "password"})

public class User extends Account {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private int bonusPoints;

    private boolean isAdult;

    public User(String firstName, String lastName, String password, LocalDate dateOfBirth, String email, int bonusPoints) {
        super(email, password);
        if(firstName == null) {
            throw new NullPointerException("Firstname cannot be null");
        }
        if(lastName == null) {
            throw new NullPointerException("Lastname cannot be null");
        }
        if(dateOfBirth == null) {
            throw new NullPointerException("Date of Birth cannot be null");
        }

        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bonusPoints = bonusPoints;
        this.isAdult = isAdult();
    }


    public int calculateAge() {
        LocalDate curDate = LocalDate.now();
        if ((dateOfBirth != null) && (curDate != null)) {
            return Period.between(dateOfBirth, curDate).getYears();
        } else {
            return 0;
        }
    }

    public boolean isAdult() { return calculateAge() >= 18; }
}
