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
    @OneToMany(mappedBy="playerData", fetch=FetchType.EAGER)
    Set<GamePlayer> PlayerSet = new HashSet<>();




    public Player() { }

    public Player(String userName) {
        this.userName = userName;
        }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public String toString() {
        return userName;
    }
}