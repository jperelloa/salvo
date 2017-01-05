package edu.example;

/**
 * Created by jpere on 07/12/2016.
 */


    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.Id;
    import javax.persistence.OneToMany;
    import javax.persistence.FetchType;
    import java.util.HashSet;
    import java.util.Set;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String userName;
    private String password;
    @OneToMany(mappedBy="playerInGp", fetch=FetchType.EAGER)
    Set<GamePlayer> playerSet = new HashSet<>();
    @OneToMany(mappedBy="playerInScore", fetch=FetchType.EAGER)
    Set<GameScore> scoreSet = new HashSet<>();





    public Player() { }

    public Player(String userName, String password) {
        this.userName = userName;
        this.password = password;
        }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<GamePlayer> getPlayerSet() {
        return playerSet;
    }

    public void setPlayerSet(Set<GamePlayer> playerSet) {
        this.playerSet = playerSet;
    }

    public String toString() {
        return userName;
    }

    public Set<GameScore> getScoreSet() {
        return scoreSet;
    }

    public void setScoreSet(Set<GameScore> scoreSet) {
        this.scoreSet = scoreSet;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}