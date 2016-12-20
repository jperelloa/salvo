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
    public CommandLineRunner initData(PlayerRepository playerrepository, GameRepository gamerepository, GamePlayerRepository gameplayerrepository) {
        CommandLineRunner commandLineRunner = (args) -> {
            // save a couple of users
            Player player1 = playerrepository.save(new Player("j.bauer@ctu.gov"));
            Player player2 = playerrepository.save(new Player("c.obrian@ctu.gov"));
            Player player3 = playerrepository.save(new Player("kim_bauer@gmail.com"));
            Player player4 = playerrepository.save(new Player("t.almeida@ctu.gov"));
            // save a couple of games

            Date date = new Date();
            Game game1 = gamerepository.save(new Game(date));


            date = Game.addTime(3600);
            Game game2 = gamerepository.save(new Game(date));
            date = Game.addTime(7200);
            Game game3 = gamerepository.save(new Game(date));
            date = Game.addTime(10800);
            Game game4 = gamerepository.save(new Game(date));
            date = Game.addTime(14400);
            Game game5 = gamerepository.save(new Game(date));
            date = Game.addTime(18000);
            Game game6 = gamerepository.save(new Game(date));


            gameplayerrepository.save(new GamePlayer(game1, player1 ));
            gameplayerrepository.save(new GamePlayer(game1, player2 ));
            gameplayerrepository.save(new GamePlayer(game2, player1 ));
            gameplayerrepository.save(new GamePlayer(game2, player2 ));
            gameplayerrepository.save(new GamePlayer(game3, player2 ));
            gameplayerrepository.save(new GamePlayer(game3, player4 ));
            gameplayerrepository.save(new GamePlayer(game4, player2 ));
            gameplayerrepository.save(new GamePlayer(game4, player1 ));
            gameplayerrepository.save(new GamePlayer(game5, player4 ));
            gameplayerrepository.save(new GamePlayer(game5, player1 ));
            gameplayerrepository.save(new GamePlayer(game6, player3 ));



        };
        return commandLineRunner;
    }





}
