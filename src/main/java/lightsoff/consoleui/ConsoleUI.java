package lightsoff.consoleui;

import lightsoff.core.Field;
import lightsoff.core.GameState;
import lightsoff.entity.Score;
import lightsoff.menu.MenuUI;
import lightsoff.service.ScoreServiceJDBC;

import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleUI {
    private Field field;
    private int attempts;
    private int level;
    private int totalScore = 0;
    private int bonusCoefficient = 1; //default value
    private String username;
    private String mode = "Simple";
    private MenuUI menuUI = new MenuUI();

    public void play(Field field){
        setField(field);
        setAttempts(field.getIlluminatedBlockCount() + (4 - bonusCoefficient));
        setLevel(field.getIlluminatedBlockCount() - 1);

        if(username == null || username.isEmpty()) {
            System.out.print("Enter please username: ");
            Scanner console = new Scanner(System.in);
            setUsername(console.nextLine());
        }

        while (field.getGameState() == GameState.PLAYING){
            if(attempts <= 0){
                field.setGameState(GameState.FAILED);
                break;
            }
            show(field);
            handleInput();
            if(field.isSolved()) field.setGameState(GameState.SOLVED);
        }

        if(field.getGameState() == GameState.SOLVED){
            System.out.println("This game is Solved!");
            setTotalScore(totalScore + bonusCoefficient);
        }
        else if(field.getGameState() == GameState.FAILED){
            System.out.println("This game is Failed!");
            return;
        }
        show(field);
        System.out.print("Do you want to start a new game? (Y/N): ");
        Scanner console = new Scanner(System.in);
        String line = console.nextLine();
        if(line.equals("Y")) {
            field.setGameState(GameState.PLAYING);
        }
        else{
            saveScore(new ScoreServiceJDBC());
        }
    }

    public void saveScore(ScoreServiceJDBC scoreServiceJDBC){
        scoreServiceJDBC.addScore(new Score("Lights off",username,totalScore,new Date()));
    }

    public void show(Field field){
        System.out.printf("level %3d\n" +
                "Mode %3s\n" +
                "Score: %3d\n" +
                "%3d attempts left\n",level, mode, totalScore, attempts);
        System.out.print("----------------------------------\n ");
        for(int k = 0;k < field.getColumnCount();k++) System.out.printf("%3d",k);
        System.out.print("\n");
        for(int i = 0;i < field.getRowCount();i++){
            System.out.print((char)(i + 65));
            for (int j = 0;j < field.getColumnCount();j++){
                System.out.printf("%12s",field.getField()[i][j].toString());
            }
            System.out.print("\n");
        }
        System.out.println("----------------------------------");
    }

    public void handleInput(){
        System.out.print("Enter please your selection: \u001B[35m<X> EXIT\u001B[0m,\u001B[36m<M> Menu\u001B[0m," +
                "\u001B[34m<GS> Game Settings\u001B[0m," +
                "\u001B[31m<LA1> Switch the tile:\u001B[0m ");
        Scanner console = new Scanner(System.in);
        String line = console.nextLine();
        readLine(line);
    }

    private void readLine(String command){
        if(command.equals("X")) {
            close();
        }
        else if(command.equals("M")){
            openMenu();
        }
        else if(command.equals("GS")){
            setUpTheGame();
        }
        else{
            Pattern pattern = Pattern.compile("L([A-Z])([0-9]+)",Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(command);
            if(matcher.matches()){
                attempts--;
                int row = (int)command.toUpperCase().charAt(1) - 65; // A -> 0. B -> 1 ....
                int column = Integer.parseInt(matcher.group(2)); // '0' -> 0, '1' -> 1
                this.field.switchTileBlock(row,column);
            }
            else System.out.println("Wrong input!");
        }
    }

    public void  setUpTheGame(){
        int mode;
        do{
            System.out.printf("Select the mode of the game:\n" +
                    "\u001B[37m1 - Simple\u001B[0m,\n" +
                    "\u001B[34m2 - Medium\u001B[0m,\n" +
                    "\u001B[35m3 - Hard\u001B[0m,\n" +
                    "\u001B[31m4 - TUKE FEI\u001B[0m: ");
            Scanner console = new Scanner(System.in);
            mode = console.nextInt();
        }while(mode < 0 || mode > 4);

        switch (mode){
            case 1:
                setAttempts(field.getIlluminatedBlockCount() + 3);
                setBonusCoefficient(1);
                setMode("Simple");
                break;
            case 2:
                setAttempts(field.getIlluminatedBlockCount() + 2);
                setBonusCoefficient(2);
                setMode("Medium");
                break;
            case 3:
                setAttempts(field.getIlluminatedBlockCount() + 1);
                setBonusCoefficient(3);
                setMode("Hard");
                break;
            case 4:
                setAttempts(field.getIlluminatedBlockCount());
                setBonusCoefficient(4);
                setMode("TUKE FEI");
                break;
        }
        field.generate();
    }
    public void openMenu(){
        menuUI.open();
    }

    public void close(){
        this.field.setGameState(GameState.FAILED);
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public void setBonusCoefficient(int bonusCoefficient) {
        this.bonusCoefficient = bonusCoefficient;
    }
}
