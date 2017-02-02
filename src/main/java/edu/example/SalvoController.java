            package edu.example;


            import org.hibernate.mapping.Array;
            import org.springframework.beans.factory.annotation.Autowired;
            import org.springframework.http.HttpStatus;
            import org.springframework.http.ResponseEntity;
            import org.springframework.security.core.Authentication;
            import org.springframework.security.core.context.SecurityContextHolder;
            import org.springframework.web.bind.annotation.*;


            import java.util.*;
            import java.util.stream.Collectors;
            import java.util.stream.Stream;

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
                    System.out.println("gpuser : " + gpUser);
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String name = auth.getName();
                    System.out.println(name);
                    int hits1 = 0;
                    int hits2 = 0;
                    if (gpUser != name) {
                        gpNonAuth();
                        System.exit(0);
                    }
                    Map<String, Object> dto = new LinkedHashMap<String, Object>();
                    GamePlayer gamePlayer = gprepo.findOne(gpId);
                    Game gameNumber = gamePlayer.getGameInGp();

                    List<Long> gps = gameNumber.getGameSet().stream()
                                            .map(gp -> gp.getId())
                                            .collect(toList());
                    Long gpAdv = 0L;
                    if (gps.get(0) == gpId) {
                         gpAdv = gps.get(1);
                    } else {
                         gpAdv = gps.get(0);
                    }
                    GamePlayer gamePlayer2 = gprepo.findOne(gpAdv);
                    int state = 0;
                    System.out.println("gpsInGAME " + gps.get(0));
                    System.out.println("gpsInGAME2 " + gps.get(1));
                    dto.put("id", gamePlayer.getGameInGp().getId());
                    dto.put("created", gamePlayer.getGameInGp().getCreationDate());
                    dto.put("gamePlayers", makeGameviewGp(gamePlayer, gameNumber));
                    dto.put("ships", makeGameviewShips(gamePlayer));
                    dto.put("salvoes", makeGameviewSalvoes(gamePlayer, gameNumber));
                    dto.put("history", makeGameviewSHistory(gamePlayer, gameNumber, gpId, gpAdv));
                    dto.put("state", makeState(gamePlayer,gamePlayer2));

                    return dto;

                }


                private void makeScore(GamePlayer gamePlayer, int state) {
                    Date date = new Date();
                    double score = 0.0;
                    Player player = gamePlayer.getPlayerInGp();
                    Game game = gamePlayer.getGameInGp();
                    if (state == 4) score = 0.5;
                    if (state == 5) score = 1.0;
                    System.out.println("STATE FINAL " + state);
                    System.out.println("SCOREEEEEEEE " + score);
                    gscorerepo.save(new GameScore(game, player, score, date));
                }


                private ResponseEntity<Map<String, Object>> gpNonAuth(){
                        Map<String, Object> dto = new LinkedHashMap<>();
                        dto.put("error", "User not authorized");
                        return new ResponseEntity<>(dto, HttpStatus.UNAUTHORIZED);
                }

                private int makeState(GamePlayer gamePlayer,GamePlayer gamePlayer2) {
                    int state = 66;
                    int shipsl = gamePlayer.getShipSet().size();
                    int ships2 = gamePlayer2.getShipSet().size();
                    int turn1 = gamePlayer.getSalvoSet().size();
                    int turn2 = gamePlayer2.getSalvoSet().size();
                    if (shipsl == 0) state = 0;
                    if (shipsl != 0 && ships2 == 0) state = 1;
                    if  ((turn1 < turn2 || turn1 == turn2) && shipsl > 0 && ships2 > 0 ) state = 2;
                    if  (turn2 < turn1 ) state = 3;
                    Set<Salvo> salvoSet = gamePlayer.getSalvoSet();
                    Set<Ship> shipSet = gamePlayer2.getShipSet();
                    boolean allSink1 = allShipsSink(salvoSet, shipSet);
                    salvoSet = gamePlayer2.getSalvoSet();
                    shipSet = gamePlayer.getShipSet();
                    boolean allSink2 = allShipsSink(salvoSet, shipSet);
                    if (allSink1 && allSink2) state = 4;
                    if (allSink1 && !allSink2 && turn1 == turn2) state = 5;
                    if (!allSink1 && allSink2 && turn1 == turn2) state = 6;
                    System.out.println("STATEEEEEE " + state);
                    makeScore(gamePlayer, state);
                    return state;
                }




                private boolean allShipsSink(Set<Salvo> salvos, Set<Ship> ships){
                    int tocados = 0;
                    for (Salvo salvo : salvos) {
                        System.out.println("SALVO LIIIIIIIIST " + salvo.getSalvoList());
                        for (Ship ship : ships) {
                            System.out.println("SHIP LIIIIIIIIST " + ship.getShipList());
                            System.out.println("SHIP GET0 " + ship.getShipList().get(0));
                            System.out.println("SHIP LONG " + ship.getShipList().size());
                            for (int i = 0; i < ship.getShipList().size(); i++) {
                                String loc = ship.getShipList().get(i);
                                if (salvo.getSalvoList().contains(loc)) {
                                     tocados = tocados + 1;
                                    System.out.println("TOCAAAAAADOOOOOS " + tocados);
                                }

                            }
                        }
                    }
                    if (tocados == 17) {
                        return true;
                    } else {
                        return false;
                    }
                }


                // endpoint players
                @RequestMapping("/players")
                public List<Object> getAllPlayers() {
                    return playerrepo
                            .findAll()
                            .stream()
                            .map(player -> makePlayerDTO(player))
                            .collect(toList());
                }
                //players POST
                @RequestMapping(path = "/players", method = RequestMethod.POST)
                public ResponseEntity<Map<String, Object>> createUser(@RequestParam String name, String pwd) {
                    if (name.isEmpty()) {
                        return new ResponseEntity<>(makeMap("error", "No name"), HttpStatus.FORBIDDEN);
                    }
                    if (pwd.isEmpty()) {
                        return new ResponseEntity<>(makeMap("error", "No password"), HttpStatus.FORBIDDEN);
                    }
                    List<Player> players = playerrepo.findByUserName(name);
                    if (players.isEmpty()) {
                        Player player = playerrepo.save(new Player(name, pwd));
                        return new ResponseEntity<>(makeMap("registered user:", player.getUserName()), HttpStatus.CREATED);
                    } else {
                        return new ResponseEntity<>(makeMap("error", "Name in use"), HttpStatus.CONFLICT);
                    }
                }


                private Map<String, Object> makeMap(String key, Object value) {
                    Map<String, Object> map = new TreeMap<>();
                    map.put(key, value);
                    return map;
                }





                //endpoint manager
                @RequestMapping("/manager")
                private Map<String, Object> getPlayerConnected() {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String name = auth.getName();
                    Player playerConnected = playerrepo.findByUserName(name).get(0);
                    Map<String, Object> dto = new TreeMap<String, Object>();
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
                    Map<String, Object> playerinfo = new TreeMap<String, Object>();
                    playerinfo.put("id", player.getId());
                    playerinfo.put("email", player.getUserName());
                    return playerinfo;
                }


                //endpoint games
                @RequestMapping(path = "/games", method = RequestMethod.POST)
                 public ResponseEntity<Map<String, Object>> createNewGame(@RequestParam String name) {
                    Date date = new Date();
                    Long numGame = grepo.count();
                    Game game = grepo.save(new Game(date));
                    Player player = playerrepo.findByUserName(name).get(0);
                    GamePlayer gameplayer = gprepo.save(new GamePlayer(game, player));
                    return new ResponseEntity<>(makeMap("Ok", "game created"), HttpStatus.CREATED);
                }


             //endpoint  /api/games/nn/players
                @RequestMapping(path = "/games/{gameId}/players", method = RequestMethod.POST)
                  public ResponseEntity<Map<String, Object>> createNewGame(@PathVariable Long gameId) {
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                        String name = auth.getName();
                        if (name == "" ) {
                            return new ResponseEntity<>(makeMap("error", "Unauthorized"), HttpStatus.UNAUTHORIZED);
                        }
                        List<Game> game = grepo.findById(gameId);
                        if (game.isEmpty()) {
                            return new ResponseEntity<>(makeMap("error", "No such game"), HttpStatus.FORBIDDEN);
                        } else {
                              Set<GamePlayer> gamePlayers = game.get(0).gameSet;
                              if (gamePlayers.size() == 2) {
                                  return new ResponseEntity<>(makeMap("error", "Game is Full"), HttpStatus.FORBIDDEN);
                              }
                              List<Object> player1 = gamePlayers.stream()
                                                         .map(gp -> takePlayerGame(gp))
                                                         .collect(toList());
                                    if (player1.get(0) == auth.getName()) {
                                        return new ResponseEntity<>(makeMap("error", "full, already member"), HttpStatus.FORBIDDEN);
                                    } else {
                                        Player player = playerrepo.findByUserName(name).get(0);
                                        Game gameNumber = grepo.findById(gameId).get(0);
                                        GamePlayer gameplayer = gprepo.save(new GamePlayer(gameNumber, player ));
                                        Long gpNumber = gameplayer.getId();
                                        return new ResponseEntity<>(makeMap("gpid", gpNumber), HttpStatus.CREATED);
                                    }

                             }
                   }

              //  private Map<String, Object> takePlayerGame(GamePlayer gamePlayer) {
                private String takePlayerGame(GamePlayer gamePlayer) {
                    String userName = gamePlayer.getPlayerInGp().getUserName();
                    return userName;
                }
            //----------------------------


            //endpoint games/players/{gamePlayerId}/ships
            @RequestMapping(path="/games/players/{gamePlayerId}/ships", method=RequestMethod.POST)
            public ResponseEntity<Map<String, Object>> createNewShip(@PathVariable Long gamePlayerId, @RequestBody List<Ship> data) {
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String name = auth.getName();
                if (name == "" ) {
                    return new ResponseEntity<>(makeMap("error1", "Unauthorized"), HttpStatus.UNAUTHORIZED);
                }
                List<GamePlayer> gpId = gprepo.findById(gamePlayerId);
                if ( gpId.size() == 0) {
                    return new ResponseEntity<>(makeMap("error2", "Unauthorized"), HttpStatus.UNAUTHORIZED);
                }

                GamePlayer gamePlayer = gprepo.findById(gamePlayerId).get(0);
                String playerInGame = gamePlayer.getPlayerInGp().getUserName();
                if (name != playerInGame) {
                    return new ResponseEntity<>(makeMap("error3", "Unauthorized"), HttpStatus.UNAUTHORIZED);
                }
                data.stream()
                        .map(ship -> makeShip(ship, gamePlayer))
                        .collect(toList());

                return new ResponseEntity<>(makeMap("error", "Created"), HttpStatus.CREATED);

                }

                private String makeShip(Ship ship, GamePlayer gamePlayer) {
                    String type = ship.getShipType();
                    List<String> location= ship.getShipList();
                    Ship newShip = shiprepo.save(new Ship(type, gamePlayer, location));
                   return "OK";
                }



                //-----------------------------------------------------


                //endpoint games/players/{gamePlayerId}/salvos
                @RequestMapping(path="/games/players/{gamePlayerId}/salvos", method=RequestMethod.POST)
                public ResponseEntity<Map<String, Object>> createNewSalvo(@PathVariable Long gamePlayerId, @RequestBody ArrayList<String> data) {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String name = auth.getName();
                    //si no está logado, no autorizado
                    if (name == "" ) {
                        return new ResponseEntity<>(makeMap("error1", "Unauthorized"), HttpStatus.UNAUTHORIZED);
                    }
                    //si no existe el gp, no autorizado
                    List<GamePlayer> gpId = gprepo.findById(gamePlayerId);
                    if ( gpId.size() == 0) {
                        return new ResponseEntity<>(makeMap("error2", "Unauthorized"), HttpStatus.UNAUTHORIZED);
                    }
                    //si no es el jugador del gp, no autorizado
                    GamePlayer gamePlayer = gprepo.findById(gamePlayerId).get(0);
                    String playerInGame = gamePlayer.getPlayerInGp().getUserName();
                    if (name != playerInGame) {
                        return new ResponseEntity<>(makeMap("error3", "Unauthorized"), HttpStatus.UNAUTHORIZED);
                    }
                    GamePlayer gamePlayer2 = null;
                    //buscamos gp del contrario y su turno
                    int turnoLocal = gamePlayer.getSalvoSet().size();
                    Game gameNumber = gamePlayer.getGameInGp();
                    List<Long> gpList = FindPlayerGames(gameNumber.gameSet);
                    if (gpList.get(0) == gamePlayerId) {
                        gamePlayer2 = gprepo.findById(gpList.get(1)).get(0);
                    } else {
                        gamePlayer2 = gprepo.findById(gpList.get(0)).get(0);
                    }
                    int turnoVisit = gamePlayer2.getSalvoSet().size();
                    //comprobar que sea num. turno correcto y su turno
                    if ( turnoLocal > turnoVisit) {
                        return new ResponseEntity<>(makeMap("error", "Forbidden"), HttpStatus.FORBIDDEN);
                    }
                    //si es turno correcto, se graba nuevo turno con su salvo
                    Long newTurn = new Long(turnoLocal + 1);
                    Salvo newSalvo = salvorepo.save(new Salvo(newTurn, gamePlayer, data));
                    return new ResponseEntity<>(makeMap("OK", "Created"), HttpStatus.CREATED);
                }

                //buscar gamePlayers del Game
                private List<Long> FindPlayerGames(Set<GamePlayer> GameSet) {
                    //creo lista vacía
                    ArrayList<Long> playerList = new ArrayList<>();
                    for (GamePlayer gp : GameSet) {
                        Long gpId =  gp.getId();
                        playerList.add(gpId);
                    }
                    return playerList;
                }




                // --------------MÉTODOS PLAYERS----------
                private Map<String, Object> makePlayerDTO(Player player) {
                    Map<String, Object> dto = new TreeMap<String, Object>();
                    dto.put("id", player.getId());
                    dto.put("email", player.getUserName());
                    dto.put("scores", lookForScores(player));
                    return dto;
                }

                private Map<String, Object> lookForScores(Player player) {
                    Map<String, Object> dto = new TreeMap<String, Object>();
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

                private Map<String, Object> makeGameViewPlayers(GamePlayer gp) {
                    Map<String, Object> dto = new TreeMap<String, Object>();
                    dto.put("id", gp.getPlayerInGp().getId());
                    dto.put("email", gp.getPlayerInGp().getUserName());
                    return dto;
                }


                //---métodos salvoes----
                private Map<Long, Object> makeGameviewSalvoes(GamePlayer gamePlayer, Game gameNumber) {
                    Map<Long, Object> dto = new TreeMap<Long, Object>();
                    for (GamePlayer gp : gameNumber.getGameSet()) {
                        dto.put(gp.getPlayerInGp().getId(), makeTurnsMap(gp));
                    }
                    return dto;
                }


                private Map<Long, Object> makeTurnsMap(GamePlayer gamePlayer) {
                    Map<Long, Object> dto = new TreeMap<Long, Object>();
                    for (Salvo salvo : gamePlayer.getSalvoSet()) {
                        dto.put(salvo.getTurnNumber(), salvo.getSalvoList());
                    }
                    return dto;
                }


                //---métodos ships------
                private List<Object> makeGameviewShips(GamePlayer gamePlayer) {
                    List<Object> playerShips = gamePlayer.getShipSet().stream()
                            .map(ship -> makeGamePlayerShipsMap(ship))
                            .collect(toList());
                    return playerShips;
                }

                private Map<String, Object> makeGamePlayerShipsMap(Ship ship) {
                    Map<String, Object> dto = new TreeMap<String, Object>();
                    dto.put("type", ship.getShipType());
                    dto.put("locations", ship.getShipList());
                    return dto;
                }


                // --------------MÉTODOS GAMES----------
                private Map<String, Object> makeGameDTO(Game game) {
                    Map<String, Object> dto = new TreeMap<String, Object>();
                    dto.put("id", game.getId());
                    dto.put("creationDate", game.getCreationDate());
                    dto.put("gamePlayers", makePlayerGameDTO(game.gameSet));
                    return dto;
                }


                // retorna una lista de objectos con los game players
                private List<Object> makePlayerGameDTO(Set<GamePlayer> gameSet) {
                    //creo lista vacía
                    List<Object> playerList = new ArrayList<>();

                    // Para cada gamePlayer / gameSet
                    // buscar el id
                    // buscar el player

                    Map<String, Object> dto;

                    List<GamePlayer> list = new ArrayList<>();
                    for (GamePlayer gamePlayer : gameSet) {
                        list.add(gamePlayer);
                    }


                    for (GamePlayer gp : list) {
                        dto = new TreeMap<String, Object>();
                        dto.put("id", gp.getId());
                        dto.put("player", makePlayerDTO(gp.getPlayerInGp(), gp));
                        playerList.add(dto);
                    }

                    return playerList;
                }


                // RETORNA DATOS DEL PLAYER
                private Map<String, Object> makePlayerDTO(Player player, GamePlayer gp) {
                    Map<String, Object> playergp = new TreeMap<String, Object>();
                    playergp.put("id", player.getId());
                    playergp.put("email", player.getUserName());
                    return playergp;
                }



                //METODOS HISTORIAL

                private Map<Long, Object> makeGameviewSHistory(GamePlayer gamePlayer, Game gameNumber, Long gpId, Long gpAdv) {
                    Map<Long, Object> dto = new TreeMap<Long, Object>();
                    for (GamePlayer gp : gameNumber.getGameSet()) {
                      //  dto.put(gp.getPlayerInGp().getId(), makeTurnsHistory(gp, gpId, gpAdv));
                       Long gpInUse =  gp.getId();
                       dto.put(gp.getPlayerInGp().getId(), findHits(gamePlayer, gpId, gpAdv, gpInUse));
                    }
                    return dto;
                }

            /*    private Map<Long, Object> makeTurnsHistory(GamePlayer gamePlayer, Long gpId, Long gpAdv) {
                    Map<Long, Object> dto = new TreeMap<Long, Object>();
                    for (Salvo salvo : gamePlayer.getSalvoSet()) {
                      Long turn =  salvo.getTurnNumber();
                      dto.put(salvo.getTurnNumber(), findHits(gamePlayer, salvo, gpId, gpAdv, turn));
                    }
                    return dto;
                }*/

              //  private List<Object> findHits(GamePlayer gamePlayer, Salvo salvo, Long gpId, Long gpAdv, Long turn) {
              private List<Object> findHits(GamePlayer gamePlayer,  Long gpId, Long gpAdv, Long gpInUse) {
                List<Object> hitList = new ArrayList<>();
                    if (gpInUse == gpId) {
                            GamePlayer gamePlayerAdv = gprepo.findById(gpAdv).get(0);
                            hitList = gamePlayerAdv.getShipSet().stream()
                                                       // .map(ship -> getHits(ship,salvo,turn, gpId))
                                                         .map(ship -> getHits(ship, gpInUse))
                                                         .collect(toList());
                    } else {
                          //  GamePlayer gamePlayerAdv = gprepo.findById(gpInUse).get(0);
                            hitList = gamePlayer.getShipSet().stream()
                                 .map(sh -> getHits(sh, gpInUse))
                                 .collect(toList());

                    }
                   return hitList;
               }



            private List<Object> getHits(Ship ship, Long gpInUse) {
                List<Object> cells = ship.getShipList().stream()
              //  List<String> cells = ship.getShipList().stream()
                                            .map(location -> compareHits(location, gpInUse))
                                          //  .filter(hit -> hit = "")
                                            .collect(toList());
                return cells;
            }


                private List<String> compareHits(String location,  Long gpInUse) {
                    System.out.println("GPINUSE  " + gpInUse);
                    ArrayList<String> hitsList = new ArrayList<>();
                    System.out.println("LOCATION " + location);
                    for (Salvo salvoant : gprepo.findOne(gpInUse).getSalvoSet()) {
                       if(salvoant.getSalvoList().contains(location)) {
                            hitsList.add(location);
                       }
                    }
                    return hitsList;
                }


            }