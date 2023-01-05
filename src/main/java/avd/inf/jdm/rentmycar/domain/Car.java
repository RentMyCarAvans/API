package avd.inf.jdm.rentmycar.domain;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type") // field on which we differentiate objects
@JsonSubTypes({
        // If value of type field equals to 'ICE' instantiate an ICE object
        @JsonSubTypes.Type(value = ICE.class, name = "ICE"),
        // If value of type field equals to 'FCEV' instantiate an FCEV object
        @JsonSubTypes.Type(value = FCEV.class, name = "FCEV"),
        // If value of type field equals to 'BEV' instantiate an BEV object
        @JsonSubTypes.Type(value = BEV.class, name = "BEV")
})

@Getter
@Setter
@NoArgsConstructor
@ToString

@Entity
@Table(name="cars")
public abstract class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "licenseplate", nullable = false)
    private String licensePlate;

    @Column(name="year_of_manufacture", nullable = false)
    private Short yearOfManufacture;

    @Column(name="model", nullable = false)
    private String model;

    @Column(name="color", nullable = true)
    private String colorType;

    @Column(name="mileage", nullable = true)
    private int mileage;

    @Column(name="number_of_seats", nullable = false)
    private int numberOfSeats;

    @Column(name="image", nullable = true)
    private String image;

    @Column(name="vehicle_type", nullable = false)
    private String vehicleType;

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Car(String licensePlate, Short yearOfManufacture, String model, String colorType, int mileage, int numberOfSeats, String image, String vehicleType, User user) {
        this.licensePlate = licensePlate;
        this.yearOfManufacture = yearOfManufacture;
        this.model = model;
        this.colorType = colorType;
        this.mileage = mileage;
        this.numberOfSeats = numberOfSeats;
        this.image = image;
        this.vehicleType = vehicleType;
        this.user = user;
    }

    public abstract double calculateTCO(int mileage, int yearOfManufacture, int numberOfSeats);
}
