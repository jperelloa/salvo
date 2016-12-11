package edu.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

    @Bean
    public CommandLineRunner initData(PlayerRepository repository) {
        return (args) -> {
            // save a couple of users
            repository.save(new Player("jperelloa@hotmail.com"));
            repository.save(new Player("pepito.grillo@hotmail.com"));
            repository.save(new Player("frodo.bolson@hotmail.com"));
            repository.save(new Player("blas.delezo@hotmail.com"));

        };
    }


}
