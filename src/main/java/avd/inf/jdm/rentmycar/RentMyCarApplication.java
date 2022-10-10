package avd.inf.jdm.rentmycar;

import avd.inf.jdm.rentmycar.domain.*;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import avd.inf.jdm.rentmycar.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Date;

@SpringBootApplication
public class RentMyCarApplication {

    private static final Logger log = LoggerFactory.getLogger((RentMyCarApplication.class));

    public static void main(String[] args) {
        SpringApplication.run(RentMyCarApplication.class, args);
    }

    @Bean
    public CommandLineRunner runRentMyCar(CarService carService, UserRepository userRepository) {
        return (args -> {
            // log
            /*
            log.debug("[RentMyCarApplication] executing method runRentMyCar()");
            User rob = new User("Rob", "Funcken","password", LocalDate.now(),"rob.funcken@avans.nl");
            User roy = new User("Roy", "Schrauwen","password", LocalDate.now(),"rob.funcken@avans.nl");
            User aubrey = new User("Aubrey", "Polderman","password", LocalDate.now(),"rob.funcken@avans.nl");

            // Save a person
            userRepository.save(rob);
            userRepository.save(roy);
            userRepository.save(aubrey);
             */

            // Create a car of category Internal Combustion Engine belonging to user Rob
            carService.save(new ICE("1ICE12", (short) 2020,"Porsche 911 Carrera GTS", ColorType.BLACK,500,2/*, rob*/));

            // Create a car of category Battery Electric Vehicle belonging to user Roy
            carService.save(new BEV("2BEV34", (short) 2021,"Lamborgini Diablo",ColorType.GREEN,1000,2/*, roy*/));

            // Create a car of category Fuel Cell Electric Vehicle belonging to user Aubrey
            carService.save(new FCEV("3FCE56", (short) 2022,"Tesla Model S",ColorType.GREY,5000,4/*, aubrey*/));
        });
    }



}
