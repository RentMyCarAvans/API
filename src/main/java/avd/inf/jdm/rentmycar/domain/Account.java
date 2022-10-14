package avd.inf.jdm.rentmycar.domain;

import lombok.*;
import org.hibernate.annotations.DiscriminatorFormula;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)

@Table(name = "Accounts")
@DiscriminatorColumn(name="Account_User",
        discriminatorType = DiscriminatorType.INTEGER)

public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String email;
    private String password;

    private boolean isAdmin;

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
