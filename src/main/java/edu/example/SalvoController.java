package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by jpere on 14/12/2016.
 */

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository grepo;


    @RequestMapping("/games")
    public List<Object> getAll() {
        return  grepo
                .findAll()
                .stream()
                .map (game -> makeGameDTO(game))
                .collect(Collectors.toList());
    }



         private Map<String, Object> makeGameDTO(Game game) {
                 Map<String, Object> dto = new LinkedHashMap<String, Object>();
                 dto.put("id", game.getId());
                 dto.put("creationDate", game.getCreationDate());
                 dto.put("gamePlayers" , makePlayerGameDTO(game.GameSet));
                 return dto;
               }


               // retorna una lista de objectos con los game players
        private List<Object> makePlayerGameDTO(Set<GamePlayer> GameSet) {
            //creo lista vacía
            List<Object> playerList = new ArrayList<>();

           // Para cada gamePlayer / GameSet
           // buscar el id
           // buscar el player

           Map<String, Object> dto;

            for (GamePlayer gp : GameSet) {
                dto = new LinkedHashMap<String, Object>();
                dto.put("id", gp.getId());
                dto.put("player", makePlayerDTO(gp.getPlayer_in_gp()));
                playerList.add(dto);
            }
            return playerList;
           }



    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> playergp = new LinkedHashMap<String, Object>();
        playergp.put("id", player.getId());
        playergp.put("email", player.getUserName());
        return playergp;
    }

    /*private List<Object> makePlayersDTO(Set<GamePlayer> GameSet) {
        List<Object> players = new ArrayList<>();
        for (GamePlayer gp : GameSet) {
            players.add(makePlayerDTO(gp.getPlayer_in_gp()));
        }
        return players;
    }*/
}