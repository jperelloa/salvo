package edu.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import java.time.Instant;
import java.util.Date;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

    @Bean
    public CommandLineRunner initData(PlayerRepository playerrepository, GameRepository gamerepository ) {
        CommandLineRunner commandLineRunner = (args) -> {
            // save a couple of users
            playerrepository.save(new Player("j.bauer@ctu.gov"));
            playerrepository.save(new Player("c.obrian@ctu.gov"));
            playerrepository.save(new Player("kim_bauer@gmail.com"));
            playerrepository.save(new Player("t.almeida@ctu.gov"));
            // save a couple of games

            Date date = new Date();
            gamerepository.save(new Game(date));
            date = Game.addTime(3600);
            gamerepository.save(new Game(date));
            date = Game.addTime(7200);
            gamerepository.save(new Game(date));

        };
        return commandLineRunner;
    }





}
