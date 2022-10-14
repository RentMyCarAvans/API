package avd.inf.jdm.rentmycar.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@DiscriminatorValue("1")

public class User extends Account {

    @NotNull
    private String firstName;
    @NotNull

    private String lastName;
    @NotNull

    private LocalDate dateOfBirth;
    private int bonusPoints;

    @JsonIgnore
    private final LocalDateTime createdAt = LocalDateTime.now();


    public User(String firstName, String lastName, String password, LocalDate dateOfBirth, String email, int bonusPoints) {
        super(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.bonusPoints = bonusPoints;
    }

}
