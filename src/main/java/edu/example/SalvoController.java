package edu.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by jpere on 14/12/2016.
 */

@RestController
@RequestMapping("/api")
public class SalvoController {

    @Autowired
    private GameRepository grepo;

    @Autowired
    private GamePlayerRepository gprepo;

    @Autowired
    private ShipRepository shiprepo;

    // endpoint games
    @RequestMapping("/games")
    public List<Object> getAll() {
        return  grepo
                .findAll()
                .stream()
                .map (game -> makeGameDTO(game))
                .collect(toList());
    }


   //endpoint game_view/gp_id
    @RequestMapping("/game_view/{gpId}")
    public Map<String, Object> getSelectgp(@PathVariable Long gpId) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        GamePlayer gamePlayer =  gprepo.findOne(gpId);
        dto.put("id", gamePlayer.getGameInGp().getId());
        dto.put("created", gamePlayer.getGameInGp().getCreationDate());
        Game gameNumber = gamePlayer.getGameInGp();
        dto.put("gamePlayers", makeGameviewGp(gamePlayer, gameNumber));
        dto.put("ships", makeGameviewShips(gamePlayer));


        return dto;

        }

    //--------------------------METODOS /gameview/nn-------------------------
    private List<Object> makeGameviewGp(GamePlayer gameplayer, Game game) {
        List<Object> playerList = game.getGameSet().stream()
                .map(gp -> makeGamePlayerMap(gp))
                .collect(toList());
        return playerList;
    }

    private Map<String, Object> makeGamePlayerMap(GamePlayer gp) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gp.getId());
        dto.put("player", makeGameViewPlayers(gp));

        return dto;
    }

    //mismo método anterior MakeGameViewGp con for
           /* for (GamePlayer gp : game.getGameSet()) {
                Map<String, Object> dto = new LinkedHashMap<String, Object>();
                dto.put("id", gp.getId());
                 // dto.put("player", makePlayerDTO(gp.getPlayerInGp()));
                playerList.add(dto);
           }*/

    private Map<String, Object> makeGameViewPlayers(GamePlayer gp) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", gp.getPlayerInGp().getId());
        dto.put("email", gp.getPlayerInGp().getUserName());
        return dto;
    }
    //---ships

    private List<Object> makeGameviewShips(GamePlayer gamePlayer) {
      //  Set<Ship> playerShips = ship;
        List<Object> playerShips = gamePlayer.getShipSet().stream()
                .map(ship -> makeGamePlayerShipsMap(ship))
               .collect(toList());
        return playerShips;
    }

     private Map<String, Object> makeGamePlayerShipsMap(Ship ship) {
            Map<String, Object> dto = new LinkedHashMap<String, Object>();
            dto.put("type", ship.getShipType() );
          //  dto.put("locations", makeShipLocations(ship));
         dto.put("locations", ship.getShipList() );
            return dto;
    }

    /*private List<Object> makeShipLocations(Ship ship) {
            List<Object> ShipLocations = ship.getShipList().stream()
                .map(loc -> ship)
                .collect(toList());
             return ShipLocations;

            return dto;
    }*/








    // --------------métodos /games -----------
    private Map<String, Object> makeGameDTO(Game game) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", game.getId());
        dto.put("creationDate", game.getCreationDate());
        dto.put("gamePlayers" , makePlayerGameDTO(game.gameSet));
        return dto;
    }


    // retorna una lista de objectos con los game players
    private List<Object> makePlayerGameDTO(Set<GamePlayer> GameSet) {
        //creo lista vacía
        List<Object> playerList = new ArrayList<>();

        // Para cada gamePlayer / gameSet
        // buscar el id
        // buscar el player

        Map<String, Object> dto;

        for (GamePlayer gp : GameSet) {
            dto = new LinkedHashMap<String, Object>();
            dto.put("id", gp.getId());
            dto.put("player", makePlayerDTO(gp.getPlayerInGp()));
            playerList.add(dto);
        }

        //TODO sugerencia: convertir a stream
        return playerList;
    }


    // RETORNA DATOS DEL PLAYER
    private Map<String, Object> makePlayerDTO(Player player) {
        Map<String, Object> playergp = new LinkedHashMap<String, Object>();
        playergp.put("id", player.getId());
        playergp.put("email", player.getUserName());
        return playergp;
    }

    // ------------------------


}