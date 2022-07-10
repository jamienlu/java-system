package reactiveuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AutoConfiguration
public class ReactiveUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveUserApplication.class, args);
	}

}
