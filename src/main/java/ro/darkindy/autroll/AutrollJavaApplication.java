package ro.darkindy.autroll;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ro.darkindy.autroll")
public class AutrollJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutrollJavaApplication.class, args);
    }

}
