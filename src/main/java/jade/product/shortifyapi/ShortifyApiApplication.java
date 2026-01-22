package jade.product.shortifyapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShortifyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortifyApiApplication.class, args);
	}

}
