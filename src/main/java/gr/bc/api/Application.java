package gr.bc.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author Konstantinos Raptis
 */
@SpringBootApplication
//@EnableConfigurationProperties(StorageProperties.class)
public class Application {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
