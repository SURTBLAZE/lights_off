package lightsoff.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

import java.io.Serializable;

@Entity
@NamedQuery(name = "GameInfo.getGameInfo",
            query = "SELECT g.info from GameInfo g WHERE g.game =:game")
@NamedQuery(name = "GameInfo.resetGameInfo",
        query = "DELETE FROM GameInfo")
public class GameInfo implements Serializable {
    @Id
    @GeneratedValue
    private int ident;
    private String game;
    private String info;

    public GameInfo(){}

    public GameInfo(String game, String info){
        this.game = game;
        this.info = info;
    }

    public String getGame() {
        return game;
    }

    public String getInfo() {
        return info;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getIdent() { return ident; }
    public void setIdent(int ident) { this.ident = ident; }
}
