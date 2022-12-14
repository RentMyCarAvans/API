package avd.inf.jdm.rentmycar;

import avd.inf.jdm.rentmycar.domain.*;
import avd.inf.jdm.rentmycar.repository.RideRepository;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import avd.inf.jdm.rentmycar.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class RentMyCarApplication {

    private static final Logger log = LoggerFactory.getLogger((RentMyCarApplication.class));

    public static void main(String[] args) {
        SpringApplication.run(RentMyCarApplication.class, args);
    }

    @Profile("!prod")
    @Bean
    public CommandLineRunner runRentMyCar(CarService carService, UserService userService, OfferService offerService, BookingService bookingService) {
        return (args -> {
            // log

            log.debug("[RentMyCarApplication] executing method runRentMyCar()");


            User user1 = new User("Rob", "Funcken","password", LocalDate.of(2000,12,31),"rob@rentmycar.nl", 100, "Hogeschoollaan 1, 1337AA", "Breda", "013-5114578", true);
            User user2 = new User("Roy", "Schrauwen","password", LocalDate.of(2000,01,01),"roy@rentmycar.nl", 100);
            User user3 = new User("Aubrey", "Polderman","password", LocalDate.of(2000,12,01),"aubrey@rentmycar.nl", 100);
            User user4 = new User("Paul", "de Mast","password", LocalDate.of(2000,01,01),"paul@avans.nl", 100);

            userService.save(user1);
            userService.save(user2);
            userService.save(user3);
            userService.save(user4);


            Car car1 = new ICE("1ICE12", (short) 2020,"Porsche 911 Carrera GTS", "BLACK",500,2,null,"personenauto",user1);
            Car car2 = new BEV("2BEV34", (short) 2021,"Lamborgini Diablo","BLACK",1000,2, null,"personenauto", user4);
            Car car3 = new FCEV("3FCE56", (short) 2022,"Tesla Model S","GREY",5000,4,null,"personenauto", user3);
            Car car4 = new ICE("3FCE57", (short) 2018,"Fiat Panda","RED",250000,3, null,"personenauto",user1);
            Car car5 = new ICE("3FCE58", (short) 2006,"Renault Kangoo","GREEN",200000,5, null,"personenauto",user2);
            Car car6 = new ICE("3FCE59", (short) 2019,"Ford Focus","GREEN",200000,5,null,"personenauto", user2);

            carService.save(car1);
            carService.save(car2);
            carService.save(car3);
            carService.save(car4);
            carService.save(car5);
            carService.save(car6);


            Offer offer1 = new Offer(LocalDateTime.parse("2023-11-01T12:00"), LocalDateTime.now().parse("2023-11-01T13:00"), "Tilburg", car2);
            Offer offer2 = new Offer(LocalDateTime.parse("2023-11-02T12:00"), LocalDateTime.now().plusYears(3).parse("2023-11-02T13:00"), "Breda", car1);
            Offer offer3 = new Offer(LocalDateTime.parse("2023-11-03T12:00"), LocalDateTime.now().plusYears(3).parse("2023-11-03T13:00"), "Vlissingen", car3);
            Offer offer4 = new Offer(LocalDateTime.parse("2023-11-04T12:00"), LocalDateTime.now().plusYears(3).parse("2023-11-04T13:00"), "Rotterdam", car4);
            Offer offer5 = new Offer(LocalDateTime.parse("2023-11-05T12:00"), LocalDateTime.now().plusYears(3).parse("2023-11-05T13:00"), "Tilburg", car5);
            Offer offer6 = new Offer(LocalDateTime.parse("2023-11-06T12:00"), LocalDateTime.now().plusYears(3).parse("2023-11-06T13:00"), "Rotterdam", car6);
            offerService.save(offer1);
            offerService.save(offer2);
            offerService.save(offer3);
            offerService.save(offer4);
            offerService.save(offer5);
            offerService.save(offer6);

            Booking booking1 = new Booking(offer1, user1);
            Booking booking2 = new Booking(offer2, user3);
            bookingService.save(booking1);
            bookingService.save(booking2);

            booking2.setStatus(BookingStatus.RETURNED);
            booking2.setDropOfLocation("Amsterdam");
            bookingService.save(booking2);

            bookingService.startRide(booking1);

            bookingService.endRide(booking1);
//            bookingService.save(booking1);


        });
    }



}
