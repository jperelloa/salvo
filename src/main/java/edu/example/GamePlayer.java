package edu.example;

/**
 * Created by jpere on 12/12/2016.
 */
import javax.persistence.*;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game gameInGp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player playerInGp;

    private Date creationDate;

    @OneToMany(mappedBy="gamePlayerInShip", fetch=FetchType.EAGER)
    Set<Ship> shipSet = new LinkedHashSet<>();


    @OneToMany(mappedBy="gamePlayerInSalvo", fetch=FetchType.EAGER)
    Set<Salvo> salvoSet = new LinkedHashSet<>();

    //private Double score;

    //private Date finishDate;


    public GamePlayer() {
    }

    //public GamePlayer(Game gameInGp, Player playerInGp, Double score, Date date ) {
    public GamePlayer(Game gameInGp, Player playerInGp) {
        this.gameInGp = gameInGp;
        this.playerInGp = playerInGp;
        this.creationDate = gameInGp.getCreationDate();
       // this.score = score;
       // this.finishDate = date;
        }

    //GETTERS & SETTERS
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Game getGameInGp() {
        return gameInGp;
    }

    public Player getPlayerInGp() {
        return playerInGp;
    }

    public void setPlayerInGp(Player playerInGp) {
        this.playerInGp = playerInGp;
    }

    public void setGameInGp(Game gameInGp) {
        this.gameInGp = gameInGp;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Set<Ship> getShipSet() {
        return shipSet;
    }

    public void setShipSet(Set<Ship> shipSet) {
        this.shipSet = shipSet;
    }

    public Set<Salvo> getSalvoSet() {
        return salvoSet;
    }

    public void setSalvoSet(Set<Salvo> salvoSet) {
        this.salvoSet = salvoSet;
    }


}