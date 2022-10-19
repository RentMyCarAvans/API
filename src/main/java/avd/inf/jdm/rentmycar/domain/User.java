package avd.inf.jdm.rentmycar.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Period;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("1")
@JsonIgnoreProperties({"hibernateLazyInitializer", "password"})

public class User extends Account {

    @NotNull
    private String firstName;
    @NotNull

    private String lastName;
    @NotNull

    private LocalDate dateOfBirth;
    private int bonusPoints;

    private boolean isAdult;
    @JsonIgnore
    private final LocalDateTime createdAt = LocalDateTime.now();


    public User(String firstName, String lastName, String password, LocalDate dateOfBirth, String email, int bonusPoints) {
        super(email, password);
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
