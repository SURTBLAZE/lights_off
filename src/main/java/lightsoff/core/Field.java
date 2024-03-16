package lightsoff.core;

public class Field {
    private Tile field[][];
    private int rowCount;
    private int columnCount;
    private int illuminatedBlockCount;
    private GameState gameState = GameState.PLAYING;

    public Field(int rowCount, int columnCount, int illuminatedBlockCount){
        if(rowCount < 0 || rowCount > 26)
            throw new IllegalArgumentException("Invalid number of rows (1-26)");
        if(columnCount < 0)
            throw new IllegalArgumentException("Invalid number of columns");
        if (rowCount * columnCount <= illuminatedBlockCount)
            throw new IllegalArgumentException("Invalid number of mines");

        this.rowCount = rowCount;
        this.columnCount = columnCount;
        this.illuminatedBlockCount = illuminatedBlockCount;
        this.field = new Tile[rowCount][columnCount];
        for(int i = 0;i < rowCount;i++){
            for(int j = 0;j < columnCount;j++){
                this.field[i][j] = new Tile(false); //by default
            }
        }
    }

    public void generate(){
        clear();
        int i = 0;
        while(i != illuminatedBlockCount){
            int x = (int)(Math.random() * rowCount);
            int y = (int)(Math.random() * columnCount);
            switchTileBlock(x,y);
            i++;
        }
    }

    public void switchTileBlock(int row,int column){
        if(row < 0 || row >= rowCount || column < 0 || column >= columnCount) return;
        for(int i = 0;i < rowCount;i++){
            for(int j = 0;j < columnCount;j++){
                if(Math.abs(i - row) + Math.abs(j - column) <= 1){
                    field[i][j].switchTile();
                }
            }
        }
    }
    public boolean isSolved(){
        for(int i = 0;i < rowCount;i++){
            for(int j = 0;j < columnCount;j++){
                if(field[i][j].getState()) return false; //if exists an Illuminated Tile
            }
        }
        return true;
    }

    public void clear(){
        for(int i = 0;i < getRowCount();i++){
            for(int j = 0;j < getColumnCount();j++){
                field[i][j].setState(false);
            }
        }
    }

    public Tile[][] getField() {
        return field;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setField(Tile field[][]) {
        this.field = field;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public void setIlluminatedBlockCount(int illuminatedCount) {
        this.illuminatedBlockCount = illuminatedCount;
    }

    public int getIlluminatedBlockCount() {
        return illuminatedBlockCount;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }
}
