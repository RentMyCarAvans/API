package avd.inf.jdm.rentmycar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<avd.inf.jdm.rentmycar.domain.Image, Long> {
    Optional<Image> findByName(String name);

}
