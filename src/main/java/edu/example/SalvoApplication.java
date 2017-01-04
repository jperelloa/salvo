package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@SpringBootApplication
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

    @Bean
    public CommandLineRunner initData(PlayerRepository playerrepository,
                                      GameRepository gamerepository,
                                      GamePlayerRepository gameplayerrepository,
                                      ShipRepository shiprepository,
                                      SalvoRepository salvorepository,
                                      GameScoreRepository gamescorerepository) {
        CommandLineRunner commandLineRunner = (args) -> {

            // save a couple of users
            Player player1 = playerrepository.save(new Player("j.bauer@ctu.gov", "password1"));
            Player player2 = playerrepository.save(new Player("c.obrian@ctu.gov", "password2"));
            Player player3 = playerrepository.save(new Player("kim_bauer@gmail.com", "password3"));
            Player player4 = playerrepository.save(new Player("t.almeida@ctu.gov", "password4"));

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

            //añadir gameplayers
           // date = new Date();
            GamePlayer gamePlayer1 = gameplayerrepository.save(new GamePlayer(game1, player1 ));
            GamePlayer gamePlayer2 = gameplayerrepository.save(new GamePlayer(game1, player2 ));
            GamePlayer gamePlayer3 = gameplayerrepository.save(new GamePlayer(game2, player1 ));
            GamePlayer gamePlayer4 = gameplayerrepository.save(new GamePlayer(game2, player2 ));
            GamePlayer gamePlayer5 = gameplayerrepository.save(new GamePlayer(game3, player2));
            GamePlayer gamePlayer6 = gameplayerrepository.save(new GamePlayer(game3, player4 ));
            gameplayerrepository.save(new GamePlayer(game4, player2 ));
            gameplayerrepository.save(new GamePlayer(game4, player1 ));
            gameplayerrepository.save(new GamePlayer(game5, player4));
            gameplayerrepository.save(new GamePlayer(game5, player1 ));
            gameplayerrepository.save(new GamePlayer(game6, player3 ));


            //barcos gamePlayer 1
            List<String> shipList1 = Arrays.asList("H2", "H3", "H4", "H5", "H6");;
            shiprepository.save(new Ship("carrier", gamePlayer1, shipList1 ));
            List<String> shipList2 = Arrays.asList("A2", "A3", "A4", "A5");;
            shiprepository.save(new Ship("battleship", gamePlayer1, shipList2 ));
            List<String> shipList3 = Arrays.asList("E4", "E5", "E6");;
            shiprepository.save(new Ship("submarine", gamePlayer1, shipList3 ));
            List<String> shipList4 = Arrays.asList("J1", "J2", "J3");;
            shiprepository.save(new Ship("destroyer", gamePlayer1, shipList4 ));
            List<String> shipList5 = Arrays.asList("F9", "G9");;
            shiprepository.save(new Ship("patrol boat", gamePlayer1, shipList5 ));


             //barcos gamePlayer 2
             shipList1 = Arrays.asList("J6", "J7", "J8", "J9", "J10");
             shiprepository.save(new Ship("carrier", gamePlayer2, shipList1 ));
             shipList2 = Arrays.asList("C2", "C3", "C4", "C5");;
             shiprepository.save(new Ship("battleship", gamePlayer2, shipList2 ));
             shipList3 = Arrays.asList("B7", "B8", "B9");;
             shiprepository.save(new Ship("submarine", gamePlayer2, shipList3 ));
             shipList4 = Arrays.asList("G7", "G8", "G9");;
             shiprepository.save(new Ship("destroyer", gamePlayer2, shipList4 ));
             shipList5 = Arrays.asList("A2", "A3");;
             shiprepository.save(new Ship("patrol boat", gamePlayer2, shipList5 ));

            //barcos gamePlayer 3
            shipList1 = Arrays.asList("F9", "G9", "H9", "I9", "J9");
            shiprepository.save(new Ship("carrier", gamePlayer3, shipList1 ));
            shipList2 = Arrays.asList("I1", "I2", "I3", "I4");;
            shiprepository.save(new Ship("battleship", gamePlayer3, shipList2 ));
            shipList3 = Arrays.asList("B1", "C1", "D1");;
            shiprepository.save(new Ship("submarine", gamePlayer3, shipList3 ));
            shipList4 = Arrays.asList("B3", "C3", "D3");;
            shiprepository.save(new Ship("destroyer", gamePlayer3, shipList4 ));
            shipList5 = Arrays.asList("C8", "C9");;
            shiprepository.save(new Ship("patrol boat", gamePlayer3, shipList5 ));

            //barcos gamePlayer 4
            shipList1 = Arrays.asList("B1", "C1", "D1", "E1", "F1");;
            shiprepository.save(new Ship("carrier", gamePlayer4, shipList1 ));
            shipList2 = Arrays.asList("J2", "J3", "J4", "J5");;
            shiprepository.save(new Ship("battleship", gamePlayer4, shipList2 ));
            shipList3 = Arrays.asList("F4", "F5", "F6");;
            shiprepository.save(new Ship("submarine", gamePlayer4, shipList3 ));
            shipList4 = Arrays.asList("A4", "A5", "A6");;
            shiprepository.save(new Ship("destroyer", gamePlayer4, shipList4 ));
            shipList5 = Arrays.asList("H7", "H8");;
            shiprepository.save(new Ship("patrol boat", gamePlayer4, shipList5 ));


            //barcos gamePlayer 5
            shipList1 = Arrays.asList("B5", "B6", "B7", "B8", "B9");
            shiprepository.save(new Ship("carrier", gamePlayer5, shipList1 ));
            shipList2 = Arrays.asList("A1", "B1", "C1", "D1");;
            shiprepository.save(new Ship("battleship", gamePlayer5, shipList2 ));
            shipList3 = Arrays.asList("F3", "G3", "H3");;
            shiprepository.save(new Ship("submarine", gamePlayer5, shipList3 ));
            shipList4 = Arrays.asList("D6", "D7", "D8");;
            shiprepository.save(new Ship("destroyer", gamePlayer5, shipList4 ));
            shipList5 = Arrays.asList("G7", "H7");;
            shiprepository.save(new Ship("patrol boat", gamePlayer5, shipList5 ));

            //barcos gamePlayer 6
            shipList1 = Arrays.asList("H4", "H5", "H6", "H7", "H8");
            shiprepository.save(new Ship("carrier", gamePlayer6, shipList1 ));
            shipList2 = Arrays.asList("F2", "F3", "F4", "F5");;
            shiprepository.save(new Ship("battleship", gamePlayer6, shipList2 ));
            shipList3 = Arrays.asList("D7", "D8", "D9");;
            shiprepository.save(new Ship("submarine", gamePlayer6, shipList3 ));
            shipList4 = Arrays.asList("A3", "A4", "A5");;
            shiprepository.save(new Ship("destroyer", gamePlayer6, shipList4 ));
            shipList5 = Arrays.asList("J7", "J8");;
            shiprepository.save(new Ship("patrol boat", gamePlayer6, shipList5 ));

            //Salvoes
            List<String> salvoList1 = Arrays.asList("B5", "C5", "F1");
            salvorepository.save(new Salvo(1L, gamePlayer1, salvoList1 ));
            List<String> salvoList1b = Arrays.asList("B4", "B5", "B6");
            salvorepository.save(new Salvo(1L, gamePlayer2, salvoList1b ));
            salvoList1 = Arrays.asList("F2", "D5");
            salvorepository.save(new Salvo(2L, gamePlayer1, salvoList1 ));
            salvoList1b = Arrays.asList("E1", "H3", "A2");
            salvorepository.save(new Salvo(2L, gamePlayer2, salvoList1b ));
            salvoList1 = Arrays.asList("A2", "A4", "G6");
            salvorepository.save(new Salvo(1L, gamePlayer3, salvoList1 ));
            salvoList1b = Arrays.asList("B5", "D5", "C7");
            salvorepository.save(new Salvo(1L, gamePlayer4, salvoList1b ));
            salvoList1 = Arrays.asList("A3", "H6");
            salvorepository.save(new Salvo(2L, gamePlayer3, salvoList1 ));
            salvoList1b = Arrays.asList("C5", "C6");
            salvorepository.save(new Salvo(2L, gamePlayer4, salvoList1b ));
            salvoList1 = Arrays.asList("G6", "H6", "A4");
            salvorepository.save(new Salvo(1L, gamePlayer5, salvoList1 ));
            salvoList1b = Arrays.asList("H1", "H2", "H3");
            salvorepository.save(new Salvo(1L, gamePlayer6, salvoList1b ));
            salvoList1 = Arrays.asList("A2", "A3","D8");
            salvorepository.save(new Salvo(2L, gamePlayer5, salvoList1 ));
            salvoList1b = Arrays.asList("E1", "F2","G3");
            salvorepository.save(new Salvo(2L, gamePlayer6, salvoList1b ));

            //crear GameScores
            date = new Date();
            gamescorerepository.save(new GameScore(game1, player1, 1.0, date));
            gamescorerepository.save(new GameScore(game1, player2, 0.0, date));
            gamescorerepository.save(new GameScore(game2, player2, 0.5, date));
            gamescorerepository.save(new GameScore(game2, player1, 0.5, date));
            gamescorerepository.save(new GameScore(game3, player2, 1.0, date));
            gamescorerepository.save(new GameScore(game3, player4, 0.0, date));
            gamescorerepository.save(new GameScore(game4, player2, 0.5, date));
          //  gamescorerepository.save(new GameScore(game4, player2, 1.0, date));
            gamescorerepository.save(new GameScore(game4, player1, 0.5, date));
            date = null;
            Double nopoints = null;
            gamescorerepository.save(new GameScore(game5, player4, nopoints, date));
            gamescorerepository.save(new GameScore(game5, player1, nopoints, date));
            gamescorerepository.save(new GameScore(game6, player3, nopoints, date));



    };
        return commandLineRunner;
    }


}

//autenticación
@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    PlayerRepository playerRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
                List<Player> people = playerRepository.findByUserName(name);
                if (!people.isEmpty()) {
                    Player player = people.get(0);
                    return new User(player.getUserName(), player.getPassword(),
                            AuthorityUtils.createAuthorityList("USER"));
                } else {
                    throw new UsernameNotFoundException("Unknown user: " + name);
                }
            }
        };
    }


//autorización
@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.anyRequest().fullyAuthenticated().
                .antMatchers("/games**").permitAll()
                .antMatchers("/api/games").permitAll()
                .antMatchers("/api/players").permitAll()
                .antMatchers("/**").hasAuthority("USER")
                .and().httpBasic();
    }

}

}