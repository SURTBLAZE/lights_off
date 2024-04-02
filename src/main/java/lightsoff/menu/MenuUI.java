package lightsoff.menu;

import lightsoff.entity.Rating;
import lightsoff.entity.Comment;
import lightsoff.service.*;
import lightsoff.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class MenuUI {
    private boolean state; //OPENED - false, CLOSED - true

    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private GameInfoService gameInfoService;

    public MenuUI(){
        this.state = false;
    }

    public void open(){
        this.state = false;
        while (!isClosed()){
            handleInput();
        }
    }

    public void handleInput(){
        System.out.print("Enter please your selection:\n" +
                "\u001B[36mGame info <I>\u001B[0m,\n" +
                "\u001B[35mShow Top scores <S>\u001B[0m,\n" +
                "\u001B[31mReset all scores <RS>\u001B[0m,\n" +
                "\u001B[33mAdd new comment <C>\u001B[0m,\n" +
                "\u001B[32mGet last 10 comments <G>\u001B[0m,\n" +
                "\u001B[36mReset all comments <RC>\u001B[0m,\n" +
                "\u001B[34mSet new rating <SR>\u001B[0m,\n" +
                "\u001B[37mGet rating <GR>\u001B[0m,\n" +
                "\u001B[35mGet average rating <GAR>\u001B[0m,\n" +
                "\u001B[31mReset all ratings <RR>\u001B[0m,\n" +
                "\u001B[33mBack to the game <B>\u001B[0m: ");
        Scanner console = new Scanner(System.in);
        String line = console.nextLine();
        readLine(line);
    }

    private void readLine(String command) {
        if(command.equals("I")){
            printGameInfo();
        }
        else if(command.equals("S")){
            printScores();
        }
        else if(command.equals("SR")){
            rate();
        }
        else if(command.equals("GR")){
            printRating();
        }
        else if(command.equals("GAR")){
            printAverageRating();
        }
        else if(command.equals("RR")){
            resetRatings();
        }
        else if(command.equals("C")){
            addNewComment();
        }
        else if(command.equals("G")){
            printComments();
        }
        else if(command.equals("RS")){
            resetScores();
        }
        else if(command.equals("RC")){
            resetComments();
        }
        else if(command.equals("B")){
             close();
        }
        else{
            System.out.println("Wrong input!");
        }
    }

    public void printGameInfo(){
        System.out.print("Enter please name of game: ");
        Scanner console = new Scanner(System.in);
        String game = console.nextLine();
        System.out.printf("Game information:\n\u001B[33m%s\u001B[0m\n",gameInfoService.getGameInfo(game));
    }

    public void printScores(){
        System.out.print("Enter please name of game: ");
        Scanner console = new Scanner(System.in);
        String game = console.nextLine();
        List<Score> scores = scoreService.getTopScores(game);
        if(scores.isEmpty()){
            System.out.println("There are no scores in database");
        }
        for(Score score : scores){
            System.out.println(score.toString());
        }
    }

    public void rate(){
        System.out.print("Enter please name of game: ");
        Scanner console = new Scanner(System.in);
        String game = console.nextLine();
        System.out.print("Enter please username: ");
        String username = console.nextLine();
        int rating;
        do {
            System.out.print("Rate please this game <1-5>: ");
            rating = console.nextInt();
        }while(rating < 1 || rating > 5);
        ratingService.setRating(new Rating(game,username,rating,new Date()));
    }

    public void printRating(){
        System.out.print("Enter please name of game: ");
        Scanner console = new Scanner(System.in);
        String game = console.nextLine();
        System.out.print("Enter please username: ");
        String username = console.nextLine();
        double rating = ratingService.getRating(game,username);
        if(rating == -1){
            System.out.println("Wrong input!");
        }
        else{
            System.out.printf("The rating of user %s in game %s is %2f\n", username,game,rating);
        }
    }
    public void printAverageRating(){
        System.out.print("Enter please name of game: ");
        Scanner console = new Scanner(System.in);
        String game = console.nextLine();
        double averageRating = ratingService.getAverageRating(game);
        if(averageRating == -1){
            System.out.println("There are no ratings or some error!");
        }
        else{
            System.out.printf("The avarege rating in game %s is %2f\n",game,averageRating);
        }
    }

    public void addNewComment(){
        System.out.print("Enter please name of game: ");
        Scanner console = new Scanner(System.in);
        String game = console.nextLine();
        System.out.print("Enter please username: ");
        String username = console.nextLine();
        System.out.print("Enter please your comment: ");
        String comment = console.nextLine();
        commentService.addComment(new Comment(game,username,comment,new Date()));
    }

    public void printComments(){
        System.out.print("Enter please name of game: ");
        Scanner console = new Scanner(System.in);
        String game = console.nextLine();
        List<Comment> comments = commentService.getComments(game);
        if(comments.isEmpty()){
            System.out.println("There are no comments in database");
        }
        for(Comment comment : comments){
            System.out.println(comment.toString());
        }
    }

    public void resetScores(){
        scoreService.reset();
        System.out.println("Scores are empty!");
    }
    public void resetComments(){
        commentService.reset();
        System.out.println("Comments are empty!");
    }

    public void resetRatings(){
        ratingService.reset();
        System.out.println("Ratings are empty!");
    }

    public void close(){
        setState(true);
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public boolean isClosed(){
        return state;
    }
}
