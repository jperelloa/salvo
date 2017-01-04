package edu.example;

/**
 * Created by jpere on 11/12/2016.
 */

import javax.persistence.*;
import java.util.*;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private Date creationDate;
    @OneToMany(mappedBy="gameInGp", fetch=FetchType.EAGER)
    Set<GamePlayer> gameSet = new LinkedHashSet<>();
    @OneToMany(mappedBy="gameInScore", fetch=FetchType.EAGER)
    Set<GameScore> scoreSet = new LinkedHashSet<>();

    public Game() { }


    public Game(Date date) {
        this.creationDate = date;
    }


    public long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }


    //método temporal para añadir tiempo a la fecha
    public static Date addTime(long seconds) {
        Date newDate = new Date();
        return Date.from(newDate.toInstant().plusSeconds(seconds));
    }

    public Set<GamePlayer> getGameSet() {
        return gameSet;
    }

    public void setGameSet(Set<GamePlayer> gameSet) {
        this.gameSet = gameSet;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<GameScore> getScoreSet() {
        return scoreSet;
    }

    public void setScoreSet(Set<GameScore> scoreSet) {
        this.scoreSet = scoreSet;
    }
}
