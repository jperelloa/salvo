package edu.example;

/**
 * Created by jpere on 07/12/2016.
 */


    import javax.persistence.Entity;
    import javax.persistence.GeneratedValue;
    import javax.persistence.GenerationType;
    import javax.persistence.Id;

@Entity
public class Player {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private String userName;


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