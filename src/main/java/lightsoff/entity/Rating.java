package lightsoff.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

import java.io.Serializable;
import java.util.Date;

@Entity
@NamedQuery(name = "Rating.updateRating",
            query = "UPDATE Rating r SET r.rating =:rating WHERE r.game =:game AND r.player =:player")
@NamedQuery(name = "Rating.getAverageRating",
            query = "SELECT avg(r.rating) FROM Rating r WHERE r.game =:game")
@NamedQuery(name = "Rating.getRating",
            query = "SELECT r.rating FROM Rating r WHERE r.game =:game AND r.player =:player")
@NamedQuery(name = "Rating.resetRatings",
            query = "DELETE FROM Rating")
public class Rating implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String player;
    private String game;
    private int rating;
    private Date ratedOn;

    public Rating(){}

    public Rating(String game, String player, int rating, Date ratedOn){
        this.game = game;
        this.player = player;
        this.rating = rating;
        this.ratedOn = ratedOn;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Date getRatedOn() {
        return ratedOn;
    }

    public void setRatedOn(Date ratedOn) {
        this.ratedOn = ratedOn;
    }

    public int getIdent() { return ident; }
    public void setIdent(int ident) { this.ident = ident; }

    @Override
    public String toString(){
        return "Rating{" +
                "game='" + game + '\'' +
                ", player='" + player + '\'' +
                ", rating=" + rating +
                ", ratedOn=" + ratedOn +
                '}';
    }
}
