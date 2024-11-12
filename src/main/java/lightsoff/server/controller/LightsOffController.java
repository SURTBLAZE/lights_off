package lightsoff.server.controller;

import lightsoff.core.Field;
import lightsoff.core.Tile;
import lightsoff.entity.Comment;
import lightsoff.entity.Rating;
import lightsoff.entity.Score;
import lightsoff.service.CommentService;
import lightsoff.service.GameInfoService;
import lightsoff.service.RatingService;
import lightsoff.service.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;
import java.util.List;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class LightsOffController {
    @Autowired
    private UserController userController;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    @Autowired
    private GameInfoService gameInfoService;
    private static String game = "Lights off";
    private Field field = new Field(7,7,2);
    public boolean isGameInfo = false;
    public boolean isTopScores = false;
    public boolean isGetComments = false;
    public boolean isAverageRating = false;
    public boolean isAddComment = false;
    public boolean isSetRating = false;
    public boolean isGetRating = false;
    public boolean isStatusMessage = false;
    public boolean addingStatus = false; //specialna premenna , aby pocas pridania komentarov a ratingov nevyskytla chyba
    public String statusMessage;
    public String mode = "Easy";
    public int level = 1;
    public int score = 0;
    private int bonusCoefficient = 1; //default value
    private int attempts;
    @RequestMapping("/lightsoff")
    public String lightsOff(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column) {
        if (row != null && column != null) {
            field.switchTileBlock(row,column);
            setAttempts(attempts - 1);
            if(field.isSolved()){
                setLevel(level + 1);
                setScore(score + bonusCoefficient);
                field = new Field(7, 7, level + 1);
                field.generate();
                setAttempts(field.getIlluminatedBlockCount() + (4 - bonusCoefficient) * 2);
            }
            else if (getAttempts() == 0) {
                addScore();
                return "redirect:/lightsoff/new";
            }
        }
        return "lightsoff";
    }

    @RequestMapping("/lightsoff/new")
    public String newGame() {
        setLevel(1);
        setScore(0);
        field = new Field(7, 7, level + 1);
        field.generate();
        setAttempts(field.getIlluminatedBlockCount() + (4 - bonusCoefficient) * 2);
        return "lightsoff";
    }

    @RequestMapping("/lightsoff/invertRow")
    public String invertRow(@RequestParam(required = false) Integer row){
        if(row != null) {
            for (int i = 0; i < 7; i++) {
                field.invertTile(row - 1, i);
            }
        }
        return "lightsoff";
    }

    @RequestMapping("/lightsoff/menu/addnewcomment")
    public String addNewComment(){
        if(!userController.isLogged()){
            setStatusMessage("You need to log in!");
            isStatusMessage = true;
            return "menu";
        }
        if(!getAddingStatus()) {
            isAddComment = true;
            setAddingStatus(true);
        }
        return "menu";
    }

    @RequestMapping("/lightsoff/backtogame")
    public String backToGame(){
        return "lightsoff";
    }

    @RequestMapping("/lightsoff/menu/getgameinfo")
    public String getGameInfo(){
        if(!getAddingStatus()) {
            isGameInfo = true;
        }
        return "menu";
    }

    private void addScore(){
        if(!userController.isLogged()){
            setStatusMessage("You can't save your score, because you are not logged");
            isStatusMessage = true;
            return;
        }
        setStatusMessage("Your score is saved!");
        isStatusMessage = true;
        scoreService.addScore(new Score(game,userController.getLoggedUser().getName(),score,new Date()));
    }
    @RequestMapping("/lightsoff/menu/gettopscores")
    public String getTopScores(){
        if(!getAddingStatus()) {
            isTopScores = true;
        }
        return "menu";
    }

    @RequestMapping("/lightsoff/menu/resetallscores")
    public String resetAllScores() {
        if(!getAddingStatus()) {
            if (!isAdmin()) {
                setStatusMessage("You need to be admin!");
                isStatusMessage = true;
                return "menu";
            }
            scoreService.reset();
            setStatusMessage("Scores table is empty!");
            isStatusMessage = true;
        }
        return "menu";
    }


    @PostMapping("/menu/addcomment")
    public String addComment(String comment){
        if(comment.isEmpty()){
            return "redirect:/lightsoff/menu/addnewcomment";
        }
        commentService.addComment(new Comment(game,userController.getLoggedUser().getName(),comment,new Date()));
        isAddComment = false;
        setAddingStatus(false);
        setStatusMessage("Comment was added successfully!");
        isStatusMessage = true;
        return "redirect:/lightsoff/menu";
    }

    @PostMapping("/menu/cancel")
    public String cancel(){
        isAddComment = false;
        isSetRating = false;
        setAddingStatus(false);
        return "redirect:/lightsoff/menu";
    }
    @RequestMapping("/lightsoff/menu/getcomments")
    public String getComments(){
        if(!getAddingStatus()) {
            isGetComments = true;
        }
        return "menu";
    }

    @RequestMapping("/lightsoff/menu/resetcomments")
    public String resetComments() {
        if(!getAddingStatus()) {
            if (!isAdmin()) {
                setStatusMessage("You need to be admin!");
                isStatusMessage = true;
                return "menu";
            }
            commentService.reset();
            setStatusMessage("Comments table is empty!");
            isStatusMessage = true;
        }
        return "menu";
    }

    @RequestMapping("/lightsoff/menu/setrating")
    public String setNewRating(){
        if(!userController.isLogged()){
            setStatusMessage("You need to log in!");
            isStatusMessage = true;
            return "menu";
        }
        if(!getAddingStatus()) {
            isSetRating = true;
            setAddingStatus(true);
        }
        return "menu";
    }

    @PostMapping("/menu/setrating")
    public String setRating(String rating){
        if(!isNumber(rating)){
            return "redirect:/lightsoff/menu/setrating";
        }
        int ratingInt = Integer.parseInt (rating);
        if(ratingInt < 1 || ratingInt > 5){
            return "redirect:/lightsoff/menu/setrating";
        }
        ratingService.setRating(new Rating(game,userController.getLoggedUser().getName(),ratingInt,new Date()));
        isSetRating = false;
        setAddingStatus(false);
        setStatusMessage("Rating was changed successfully!");
        isStatusMessage = true;
        return "redirect:/lightsoff/menu";
    }

    @RequestMapping("/lightsoff/menu/getrating")
    public String getRating(){
        if(!userController.isLogged()){
            setStatusMessage("You need to log in!");
            isStatusMessage = true;
            return "menu";
        }
        if(!getAddingStatus()) {
            isGetRating = true;
        }
        return "menu";
    }

    @RequestMapping("/lightsoff/menu/getaveragerating")
    public String getAverageRating(){
        if(!getAddingStatus()) {
            isAverageRating = true;
        }
        return "menu";
    }

    @RequestMapping("/lightsoff/menu/resetratings")
    public String resetRatings() {
        if(!getAddingStatus()) {
            if (!isAdmin()) {
                setStatusMessage("You need to be admin!");
                isStatusMessage = true;
                return "menu";
            }
            ratingService.reset();
            setStatusMessage("Ratings table is empty!");
            isStatusMessage = true;
        }
        return "menu";
    }

    public String printAverageRating(){
        int rating = ratingService.getAverageRating(game);
        StringBuilder sb = new StringBuilder();
        if(rating == -1){
            sb.append("<p>No one has rated the game yet");
        }
        else{
            sb.append("<p>Average rating is: " + rating);
        }
        sb.append("</p>\n");
        isAverageRating = false;
        return sb.toString();
    }

    public String printTopScores(){
        List<Score> scores = scoreService.getTopScores(game);
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Top scores</h2>");
        sb.append(" <div class='res-table'>\n" +
                "        <div class='res-table-head'>\n" +
                "                <p>Game</p>\n" +
                "                <p>Player</p>\n" +
                "                <p>Points</p>\n" +
                "                <p>Played at</p>\n" +
                "        </div>\n" +
                "        <div class='res-table-body'>\n");
        for (Score score : scores){
            sb.append("<div class='res-table-body-block'>\n" +
                    "            <p>" + score.getGame() +"</p>\n" +
                    "            <p>" + score.getPlayer() +"</p>\n" +
                    "            <p>" + score.getPoints() +"</p>\n" +
                    "            <p>" + score.getPlayedOn() +"</p>\n" +
                    " </div>\n");
        }
        sb.append("</div>\n</div>\n");
        isTopScores = false;
        return sb.toString();
    }

    public String printComments(){
        List<Comment> comments = commentService.getComments(game);
        StringBuilder sb = new StringBuilder();
        sb.append("<h2>Comments</h2>");
        sb.append(" <div class='res-table'>\n" +
                "        <div class='res-table-head'>\n" +
                "                <p>Game</p>\n" +
                "                <p>Player</p>\n" +
                "                <p>Comment</p>\n" +
                "                <p>Commented at</p>\n" +
                "        </div>\n" +
                "        <div class='res-table-body'>\n");
        for (Comment comment : comments){
            sb.append("<div class='res-table-body-block'>\n" +
                    "            <p>" + comment.getGame() +"</p>\n" +
                    "            <p>" + comment.getPlayer() +"</p>\n" +
                    "            <p>" + comment.getComment() +"</p>\n" +
                    "            <p>" + comment.getCommentedOn() +"</p>\n" +
                    " </div>\n");
        }
        sb.append("</div>\n</div>\n");
        isGetComments = false;
        return sb.toString();
    }

    public String printGameInfo(){
        String gameInfo = gameInfoService.getGameInfo(game);
        StringBuilder sb = new StringBuilder();
        sb.append("<p>");
        sb.append(gameInfo);
        sb.append("</p>\n");
        isGameInfo = false;
        return sb.toString();
    }

    public String printUserRating(){
        int rating = ratingService.getRating(game,userController.getLoggedUser().getName());
        StringBuilder sb = new StringBuilder();
        if(rating == -1){
            sb.append("<p>You haven't rated the game yet");
        }
        else{
            sb.append("<p>You rated the game " + rating + " points");
        }
        sb.append("</p>\n");
        isGetRating = false;
        return sb.toString();
    }

    @RequestMapping("/lightsoff/menu")
    public String openMenu(){
        return "menu";
    }

    @RequestMapping("/lightsoff/login")
    public String openLogin(){
        if(userController.isLogged()){
            return "redirect:/lightsoff/new";
        }
        return "login";
    }

    @RequestMapping("/lightsoff/gamesettings")
    public String openGameSettings(){
        return "gamesettings";
    }

    @RequestMapping("/lightsoff/gamesettings/easy")
    public String changeModeEasy(){
        setMode("Easy");
        setBonusCoefficient(1);
        setAttempts(field.getIlluminatedBlockCount() + 3);
        field.generate();
        return "gamesettings";
    }

    @RequestMapping("/lightsoff/gamesettings/normal")
    public String changeModeNormal(){
        setMode("Normal");
        setBonusCoefficient(2);
        setAttempts(field.getIlluminatedBlockCount() + 2);
        field.generate();
        return "gamesettings";
    }
    @RequestMapping("/lightsoff/gamesettings/hard")
    public String changeModeHard(){
        setMode("Hard");
        setBonusCoefficient(3);
        setAttempts(field.getIlluminatedBlockCount() + 1);
        field.generate();
        return "gamesettings";
    }

    @RequestMapping("/lightsoff/gamesettings/tukefei")
    public String changeModeTukeFei(){
        setMode("TUKE FEI");
        setBonusCoefficient(4);
        setAttempts(field.getIlluminatedBlockCount());
        field.generate();
        return "gamesettings";
    }

    public String getState() {
        return field.getGameState().toString();
    }

    public String getHtmlField() {
        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                var tile = field.getTile(row,column);
                sb.append("<td>\n");
                sb.append("<a href='/lightsoff?row=" + row + "&column=" + column + "'>\n");
                sb.append("<img src='/images/" + getImageName(tile) + ".jpg' width='50' height='50' >");
                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");
        return sb.toString();
    }

    public String getInvertButtons(){
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='invertButtons'>");
        for(int i = 1;i <= 7;i++){
            sb.append(" <a href='/lightsoff/invertRow?row=" + i + "'>Invert Row</a>");
        }
        sb.append("</div>");

        return sb.toString();
    }

    public boolean getAddingStatus(){
        return addingStatus;
    }

    public void setAddingStatus(boolean status){
        addingStatus = status;
    }

    public String printStatusMessage(){
        StringBuilder sb = new StringBuilder();
        sb.append("<p>");
        sb.append(statusMessage);
        sb.append("</p>\n");
        isStatusMessage = false;
        return sb.toString();
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setBonusCoefficient(int bonusCoefficient) {
        this.bonusCoefficient = bonusCoefficient;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    private String getImageName(Tile tile) {
        if(tile.getState()){
            return "illuminated";
        } else{
            return "dark";
        }
    }

    private boolean isAdmin(){
        if(userController.isLogged()){
            if(userController.getLoggedUser().getName().equals("admin") && userController.getLoggedUser().getPasswd().equals("admin")){
                return true;
            }
        }
        return false;
    }

    public String getModeImage(){
        StringBuilder sb = new StringBuilder();
        if(mode.equals("Easy")){
            sb.append("<img src='/images/easy.jpg' width='200' height='200' >");
        }
        else if(mode.equals("Normal")){
            sb.append("<img src='/images/normal.jpg' width='200' height='200' >");
        }
        else if(mode.equals("Hard")){
            sb.append("<img src='/images/hard.jpg' width='200' height='200' >");
        }
        else if(mode.equals("TUKE FEI")){
            sb.append("<img src='/images/tukefei.jpg' width='200' height='200' >");
        }
        return sb.toString();
    }

    public String getLevelInfo(){
        StringBuilder sb = new StringBuilder();
        if(userController.isLogged()) {
            sb.append("<p class='game-info'>Player:  " + userController.getLoggedUser().getName() + "</p>\n");
        }
        else{
            sb.append("<p class='game-info'>Player: Unverified</p>\n");
        }
        sb.append("<p class='game-info'>Level:  " + level + "</p>\n");
        sb.append("<p class='game-info'>Mode:  " + mode + "</p>\n");
        sb.append("<p class='game-info'>Score:  " + score + "</p>\n");
        sb.append("<p class='game-info'>Attempts:  " + attempts + "</p>\n");
        return sb.toString();
    }

    private static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

}
