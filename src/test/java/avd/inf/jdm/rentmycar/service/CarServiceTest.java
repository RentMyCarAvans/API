package avd.inf.jdm.rentmycar.service;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import avd.inf.jdm.rentmycar.domain.Car;
import avd.inf.jdm.rentmycar.domain.ColorType;
import avd.inf.jdm.rentmycar.domain.ICE;
import avd.inf.jdm.rentmycar.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @Mock
    CarService carService;

    @Test
    public void testValidLicensePlate() {
        Boolean validLicensePlate = true;
        when(carService.isValidLicensePlate("HF067X")).thenReturn(validLicensePlate);
        assertEquals(validLicensePlate, carService.isValidLicensePlate("HF067X"));
    }

    @Test
    public void testInvalidLicensePlate() {
        Boolean validLicensePlate = true;
        when(carService.isValidLicensePlate("IN-VAL-ID")).thenReturn(validLicensePlate);
        assertEquals(validLicensePlate, carService.isValidLicensePlate("IN-VAL-ID"));
    }
    @Test
    public void testgetCarById() {
        User user = new User("Demo","Testname","password", LocalDate.now(), "email@gmail.com",0);
        Car car = new ICE("1ICE12", (short) 2020,"Porsche 911 Carrera GTS", ColorType.BLACK,500,2,user);
        when(carService.getById(1L)).thenReturn(car);
        Car car2 = carService.getById(1L);
        assertEquals(car, car2);
    }
}
