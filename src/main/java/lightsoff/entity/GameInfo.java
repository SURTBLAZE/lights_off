package lightsoff.entity;

public class GameInfo {
    private String game;
    private String info;

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
}
