            package edu.example;

            import org.springframework.beans.factory.annotation.Autowired;
            import org.springframework.http.HttpStatus;
            import org.springframework.http.ResponseEntity;
            import org.springframework.security.core.Authentication;
            import org.springframework.security.core.context.SecurityContextHolder;
            import org.springframework.web.bind.annotation.*;
            import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
            import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;


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
                private PlayerRepository playerrepo;

                @Autowired
                private GamePlayerRepository gprepo;

                @Autowired
                private ShipRepository shiprepo;

                @Autowired
                private SalvoRepository salvorepo;

                @Autowired
                private GameScoreRepository gscorerepo;



                // endpoint games
                @RequestMapping("/games")
                public List<Object> getAll() {
                    return  grepo
                            .findAll()
                            .stream()
                            .map(game -> makeGameDTO(game))
                            .collect(toList());
                }


                //endpoint game_view/gp_id
                @RequestMapping("/game_view/{gpId}")
                 public Map<String, Object> getSelectgp(@PathVariable Long gpId) {
                    String gpUser = gprepo.findById(gpId).get(0).getPlayerInGp().getUserName();
                    System.out.println(gpUser);
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String name = auth.getName();
                    System.out.println(name);
                    if (gpUser != name) {
                        gpNonAuth();
                        System.exit(0);
                    }
                        Map<String, Object> dto = new LinkedHashMap<String, Object>();
                        GamePlayer gamePlayer = gprepo.findOne(gpId);
                        dto.put("id", gamePlayer.getGameInGp().getId());
                        dto.put("created", gamePlayer.getGameInGp().getCreationDate());
                        Game gameNumber = gamePlayer.getGameInGp();
                        dto.put("gamePlayers", makeGameviewGp(gamePlayer, gameNumber));
                        dto.put("ships", makeGameviewShips(gamePlayer));
                        dto.put("salvoes", makeGameviewSalvoes(gamePlayer, gameNumber));
                        return dto;
                    }

                private ResponseEntity<Map<String, Object>> gpNonAuth(){
                        Map<String, Object> dto = new LinkedHashMap<>();
                        dto.put("error", "User not authorized");
                        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
                }


                // endpoint players
                @RequestMapping("/players")
                public List<Object> getAllPlayers() {
                    System.out.println("PLAYER 1 ");
                    return playerrepo
                            .findAll()
                            .stream()
                            .map(player -> makePlayerDTO(player))
                            .collect(toList());
                }

                @RequestMapping(path = "/players", method = RequestMethod.POST)
                public ResponseEntity<Map<String, Object>> createUser(@RequestParam String name, String pwd) {
                    System.out.println("name " + name);
                    System.out.println("pwd " + pwd);
                    System.out.println("PLAYER POST");
                    if (name.isEmpty()) {
                        return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
                    }
                    if (pwd.isEmpty()) {
                        return new ResponseEntity<>(makeMap("error", "No password"), HttpStatus.FORBIDDEN);
                    }
                    List<Player> players = playerrepo.findByUserName(name);
                    System.out.println("players " + players);
                    if (players.isEmpty()) {
                        System.out.println("nuevo");
                        Player player = playerrepo.save(new Player(name, pwd));
                        System.out.println("usuario: " + player.getUserName());
                        return new ResponseEntity<>(makeMap("registered user:", player.getUserName()), HttpStatus.CREATED);
                    } else {
                        System.out.println("REPE");
                        return new ResponseEntity<>(makeMap("error", "Name in use"), HttpStatus.CONFLICT);
                    }
                }

                private Map<String, Object> makeMap(String key, Object value) {
                    Map<String, Object> map = new HashMap<>();
                    map.put(key, value);
                    return map;
                }

                @RequestMapping(path = "/login", method = RequestMethod.POST)
                public ResponseEntity<Map<String, Object>> loginUser(@RequestParam String name, String pwd) {
                    System.out.println("name " + name);
                    System.out.println("pwd " + pwd);
                    System.out.println("PLAYER POST");
                    if (name.isEmpty()) {
                        return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
                    }
                 else {
                    System.out.println("REPE");
                    return new ResponseEntity<>(makeMap("error", "Name in use"), HttpStatus.CONFLICT);
                }
                }


                @RequestMapping("/manager")
                private Map<String, Object> getPlayerConnected() {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String name = auth.getName();
                    Player playerConnected = playerrepo.findByUserName(name).get(0);
                    Map<String, Object> dto = new LinkedHashMap<String, Object>();
                        if (name != null) {
                            dto.put("player", makePlayerLoggedInfo(playerConnected));
                        } else {
                            dto.put("player", null);
                        }
                        dto.put("games", getGame());

                        return dto;
                    }

                public List<Object> getGame() {
                    return  grepo
                            .findAll()
                            .stream()
                            .map(game -> makeGameDTO(game))
                            .collect(toList());
                }

                private Map<String, Object> makePlayerLoggedInfo(Player player) {
                    Map<String, Object> playerinfo = new LinkedHashMap<String, Object>();
                    playerinfo.put("id", player.getId());
                    playerinfo.put("email", player.getUserName());
                    return playerinfo;
                }



           /* @RequestMapping(path = "/game.html", method = RequestMethod.POST)
            public ResponseEntity<Map<String, Object>> numGp(@RequestParam Long gp) {
              //  if (gp.isEmpty()) {
                System.out.println("GP " + gp);
                if (gp == 6) {
                    System.out.println("HOOOLLAAAAAA");

                    Map<String, Object> dto = new LinkedHashMap<>();
                    dto.put("error", "User not authenticated");
                    return new ResponseEntity<>(dto, HttpStatus.FORBIDDEN);

                }

                 GamePlayer gamePlayer = gprepo.findById(gp).get(0);
                 if (gp != null) {
                    return new ResponseEntity<>("Name already used", HttpStatus.CONFLICT);
                   //  return new ResponseEntity<>(makeMap("error", "No such user"), HttpStatus.CONFLICT);
                }

               /* userRepository.save(new User(name));.
                   return new ResponseEntity<>("Named added", HttpStatus.CREATED);
                }*/




                // --------------MÉTODOS PLAYERS----------
                private Map<String, Object> makePlayerDTO(Player player) {
                    Map<String, Object> dto = new LinkedHashMap<String, Object>();
                    dto.put("id", player.getId());
                    dto.put("email", player.getUserName());
                    dto.put("scores", lookForScores(player));
                    return dto;
                }

                private Map<String, Object> lookForScores(Player player) {
                    Map<String, Object> dto = new LinkedHashMap<String, Object>();
                    Double total = 0.0;
                    Double won = 0.0;
                    Double lost = 0.0;
                    Double tied = 0.0;
                    for (GameScore score : player.getScoreSet()) {
                        if (score.getScore() != null) {
                            total += score.getScore();
                            if (score.getScore() == 1) {
                                won += 1;
                            }
                            if (score.getScore() == 0) {
                                lost += 1;
                            }
                            if (score.getScore() == 0.5) {
                                tied += 1;
                            }
                        }
                    }
                    dto.put("total", total);
                    dto.put("won", won);
                    dto.put("lost", lost);
                    dto.put("tied", tied);
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
                    Map<String, Object> dto = new TreeMap<String, Object>();
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

                //---métodos salvoes----
                private Map<Long, Object> makeGameviewSalvoes(GamePlayer gamePlayer, Game gameNumber) {
                    Map<Long, Object> dto = new LinkedHashMap<Long, Object>();
                    for (GamePlayer gp : gameNumber.getGameSet()) {
                        dto.put(gp.getPlayerInGp().getId(), makeTurnsMap(gp));
                    }
                    return dto;
                }


                private Map<Long, Object> makeTurnsMap(GamePlayer gamePlayer) {
                    Map<Long, Object> dto = new LinkedHashMap<Long, Object>();
                    for (Salvo salvo : gamePlayer.getSalvoSet()) {
                        dto.put(salvo.getTurnNumber(), salvo.getSalvoList());
                    }
                    return dto;
                }


                //---ships------
                private List<Object> makeGameviewShips(GamePlayer gamePlayer) {
                    List<Object> playerShips = gamePlayer.getShipSet().stream()
                            .map(ship -> makeGamePlayerShipsMap(ship))
                            .collect(toList());
                    return playerShips;
                }

                private Map<String, Object> makeGamePlayerShipsMap(Ship ship) {
                    Map<String, Object> dto = new LinkedHashMap<String, Object>();
                    dto.put("type", ship.getShipType());
                    dto.put("locations", ship.getShipList());
                    return dto;
                }


                // --------------MÉTODOS GAMES----------
                private Map<String, Object> makeGameDTO(Game game) {
                    Map<String, Object> dto = new LinkedHashMap<String, Object>();
                    dto.put("id", game.getId());
                    dto.put("creationDate", game.getCreationDate());
                    dto.put("gamePlayers", makePlayerGameDTO(game.gameSet));
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
                        dto.put("player", makePlayerDTO(gp.getPlayerInGp(), gp));
                        playerList.add(dto);
                    }

                    return playerList;
                }


                // RETORNA DATOS DEL PLAYER
                private Map<String, Object> makePlayerDTO(Player player, GamePlayer gp) {
                    Map<String, Object> playergp = new LinkedHashMap<String, Object>();
                    playergp.put("id", player.getId());
                    playergp.put("email", player.getUserName());
                    return playergp;
                }




            }