package edu.example;

/**
 * Created by jpere on 12/12/2016.
 */
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.FetchType;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
public class GamePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game_in_gp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player_in_gp;

    private Date creationDate;


    public GamePlayer() {
    }

    public GamePlayer(Game game_in_gp, Player player_in_gp) {
        this.game_in_gp = game_in_gp;
        this.player_in_gp = player_in_gp;
        this.creationDate = game_in_gp.getCreationDate();
    }

    //GETTERS & SETTERS
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Player getPlayer_in_gp() {
        return player_in_gp;
    }

    public void setPlayer_in_gp(Player player_in_gp) {
        this.player_in_gp = player_in_gp;
    }

    public Game getGame_in_gp() {
        return game_in_gp;
    }

    public void setGame_in_gp(Game game_in_gp) {
        this.game_in_gp = game_in_gp;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /*public Map<String, Object> getMap () {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("id", getId());
        dto.put("player", getPlayer_in_gp());
        dto.put("game", getGame_in_gp());
        dto.put("creation date", getCreationDate());
        return dto;
    }*/


}