package avd.inf.jdm.rentmycar.integration;

import avd.inf.jdm.rentmycar.domain.User;
import avd.inf.jdm.rentmycar.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class UserRepositoryIntegrationTests {
    @Autowired
    private UserRepository userRepository;

    User testUser1 = new User("Test", "The Tester", "welkom", LocalDate.of(2022,01,01), "test+thetester@rentmycar.nl", 100);
    User testUser2 = new User("Test", "The Tester", "welkom", LocalDate.of(2022,01,01), "test+thetester@rentmycar.nl", 100);
@Test
    public void whenDeletingUserFromRepository_DeletingShouldSucceed() {
        long countBefore = userRepository.count();
        userRepository.save(testUser1);
        userRepository.save(testUser2);

        //act
        userRepository.deleteById(testUser1.getId());

        //assert
        assertThat(userRepository.count()).isEqualTo(countBefore + 1);
    }
}
