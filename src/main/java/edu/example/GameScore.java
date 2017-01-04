package edu.example;

/**
 * Created by jpere on 02/01/2017.
 */
import javax.persistence.*;
import java.util.Date;


@Entity
public class GameScore {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Game gameInScore;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player playerInScore;

    private Double score;

    private Date finishDate;


    public GameScore() {
    }

    public GameScore(Game gameInScore, Player playerInScore, Double score, Date date) {
        this.gameInScore = gameInScore;
        this.playerInScore = playerInScore;
        this.score = score;
        this.finishDate = date;
    }


    //GETTERS & SETTERS


    public Game getGameInScore() {
        return gameInScore;
    }

    public void setGameInScore(Game gameInScore) {
        this.gameInScore = gameInScore;
    }

    public Player getPlayerInScore() {
        return playerInScore;
    }

    public void setPlayerInScore(Player playerInScore) {
        this.playerInScore = playerInScore;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
}
