package edu.example;

/**
 * Created by jpere on 28/12/2016.
 */

import javax.persistence.*;
import java.util.*;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private Long turnNumber;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayerInSalvo;
    @ElementCollection
    @Column(name="salvo")
    private List<String> salvoList = new ArrayList<>();

    public Salvo() { }

    public Salvo(Long turnNumber, GamePlayer gamePlayerInSalvo, List<String> salvoList) {
        this.turnNumber = turnNumber;
        this.gamePlayerInSalvo = gamePlayerInSalvo;
        this.salvoList = salvoList;
    }

    public Long getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(Long turnNumber) {
        this.turnNumber = turnNumber;
    }

    public List<String> getSalvoList() {
        return salvoList;
    }

    public void setSalvoList(List<String> salvoList) {
        this.salvoList = salvoList;
    }

    public GamePlayer getGamePlayerInSalvo() {

        return gamePlayerInSalvo;
    }

    public void setGamePlayerInSalvo(GamePlayer gamePlayerInSalvo) {
        this.gamePlayerInSalvo = gamePlayerInSalvo;
    }
}

