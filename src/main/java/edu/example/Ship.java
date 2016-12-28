package edu.example;

/**
 * Created by jpere on 20/12/2016.
 */
import javax.persistence.*;
import java.util.*;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String shipType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayerInShip;
    @ElementCollection
    @Column(name="ship")
    private List<String> shipList = new ArrayList<>();

    public Ship() { }

    public Ship(String shipType, GamePlayer gamePlayerInShip, List<String> shipList) {
            this.shipType = shipType;
            this.gamePlayerInShip = gamePlayerInShip;
            this.shipList = shipList;
        }

    public String getShipType() {
        return shipType;
    }

    public void setShipType(String shipType) {
        this.shipType = shipType;
    }

    public GamePlayer getGamePlayerInShip() {
        return gamePlayerInShip;
    }

    public void setGamePlayerInShip(GamePlayer gamePlayerInShip) {
        this.gamePlayerInShip = gamePlayerInShip;
    }

    public List<String> getShipList() {
        return shipList;
    }

    public void setShipList(List<String> shipList) {
        this.shipList = shipList;
    }
}
