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
    @OneToMany(mappedBy="game_in_gp", fetch=FetchType.EAGER)
    Set<GamePlayer> GameSet = new LinkedHashSet<>();

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


    public static Date addTime(long seconds) {
        Date newDate = new Date();
        return Date.from(newDate.toInstant().plusSeconds(seconds));
    }



}
