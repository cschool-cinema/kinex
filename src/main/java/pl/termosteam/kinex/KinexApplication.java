package pl.termosteam.kinex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

/**
 * The Cinema backend api with
 * JWT authentication system and basic crud and report functionality
 *
 * @author Paweł Szopiński {Entities, Repository and DTO with controller requests}
 * @author Dmytyro Lumelskyj {Security module and related entities, DTO, rest controller and email services}
 * @author Jacek Gas {Application unit and integration tests}
 * @version 1.0
 * @since 2020-01-01
 */

@SpringBootApplication
public class KinexApplication {

    public static void main(String[] args) {
        SpringApplication.run(KinexApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}
