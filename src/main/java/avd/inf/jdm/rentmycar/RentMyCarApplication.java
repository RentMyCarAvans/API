package avd.inf.jdm.rentmycar;

import avd.inf.jdm.rentmycar.domain.*;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import avd.inf.jdm.rentmycar.service.BookingService;
import avd.inf.jdm.rentmycar.service.CarService;
import avd.inf.jdm.rentmycar.service.OfferService;
import avd.inf.jdm.rentmycar.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class RentMyCarApplication {

    private static final Logger log = LoggerFactory.getLogger((RentMyCarApplication.class));

    public static void main(String[] args) {
        SpringApplication.run(RentMyCarApplication.class, args);
    }

    @Bean
    public CommandLineRunner runRentMyCar(CarService carService, UserService userService, OfferService offerService, BookingService bookingService) {
        return (args -> {
            // log

            log.debug("[RentMyCarApplication] executing method runRentMyCar()");
            User rob = new User("Rob", "Funcken","password", LocalDate.now(),"rob.funcken@avans.nl", 100);
            User roy = new User("Roy", "Schrauwen","password", LocalDate.now(),"rob.funcken@avans.nl", 100);
            User aubrey = new User("Aubrey", "Polderman","password", LocalDate.now(),"rob.funcken@avans.nl", 100);

            // Save a person
            userService.save(rob);
            userService.save(roy);
            userService.save(aubrey);


            // Create a car of category Internal Combustion Engine belonging to user Rob
            Car car1 = new ICE("1ICE12", (short) 2020,"Porsche 911 Carrera GTS", ColorType.BLACK,500,2,rob);
            carService.save(car1);
            // Create a car of category Battery Electric Vehicle belonging to user Roy
            Car car2 =  new BEV("2BEV34", (short) 2021,"Lamborgini Diablo",ColorType.GREEN,1000,2, roy);
            carService.save(car2);
            // Create a car of category Fuel Cell Electric Vehicle belonging to user Aubrey
            Car car3 = new FCEV("3FCE56", (short) 2022,"Tesla Model S",ColorType.GREY,5000,4, aubrey);
            carService.save(car3);

            Offer offer1 = new Offer(LocalDateTime.now().plusYears(3), LocalDateTime.now().plusYears(4), "Tilburg", car2);
            Offer offer2 = new Offer(LocalDateTime.now().plusYears(3).plusHours(1), LocalDateTime.now().plusYears(3).plusHours(2), "Breda", car1);
            Offer offer3 = new Offer(LocalDateTime.now().plusYears(3).plusDays(1), LocalDateTime.now().plusYears(3).plusDays(1).plusHours(1), "Rotterdam", car1);
            offerService.save(offer1);
            offerService.save(offer2);
            offerService.save(offer3);

            Booking booking1 = new Booking(offer1, rob);
            Booking booking2 = new Booking(offer2, aubrey);
            bookingService.save(booking1);
            bookingService.save(booking2);

            booking2.setStatus(BookingStatus.RETURNED);
            booking2.setDropOfLocation("Amsterdam");
            bookingService.save(booking2);


        });
    }



}
