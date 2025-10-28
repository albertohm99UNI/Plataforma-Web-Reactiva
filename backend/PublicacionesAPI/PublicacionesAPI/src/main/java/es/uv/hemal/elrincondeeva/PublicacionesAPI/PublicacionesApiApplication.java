package es.uv.hemal.elrincondeeva.PublicacionesAPI;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableScheduling
public class PublicacionesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PublicacionesApiApplication.class, args);
	}

}
