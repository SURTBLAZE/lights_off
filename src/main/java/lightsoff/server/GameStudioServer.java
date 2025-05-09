package lightsoff.server;

import lightsoff.server.controller.LightsOffController;
import lightsoff.server.controller.UserController;
import lightsoff.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
//@Configuration
@EntityScan(basePackages = "lightsoff.entity")
public class GameStudioServer {
    public static void main(String[] args) {
        SpringApplication.run(GameStudioServer.class, args);
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceJPA();
    }

    @Bean
    public CommentService commentService(){
        return new CommentServiceJPA();
    }

    @Bean
    public RatingService ratingService(){
        return new RatingServiceJPA();
    }

    @Bean
    public GameInfoService gameInfoService(){
        return new GameInfoServiceJPA();
    }

    @Bean
    public LightsOffController lightsOffController(){
        return new LightsOffController();
    }

    @Bean
    public UserController userController(){
        return new UserController();
    }
}