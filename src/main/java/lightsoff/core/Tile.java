package lightsoff.core;

public class Tile {
    private boolean state;

    public Tile(boolean state){
        this.state = state;
    }

    public void switchTile(){
        this.state = !state;
    }

    public boolean getState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString(){
        if(!state) return "\u001B[37m" + "#" + "\u001B[0m";
        else return "\u001B[33m" + "+" + "\u001B[0m";
    }
}
