package avd.inf.jdm.rentmycar.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name="cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String licensePlate;
    private Short yearOfManufacture;
    private String model;
    private String color;
    private int mileage;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    public Car(String licensePlate, Short yearOfManufacture, String model, String color, int mileage) {
        this.licensePlate = licensePlate;
        this.yearOfManufacture = yearOfManufacture;
        this.model = model;
        this.color = color;
        this.mileage = mileage;
    }
}
