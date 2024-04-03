package lightsoff;

import lightsoff.consoleui.ConsoleUI;
import lightsoff.core.Field;
import lightsoff.menu.MenuUI;
import lightsoff.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@Configuration
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX,
        pattern = "lightsoff.server.*"))
public class SpringClient {
    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringClient.class).web(WebApplicationType.NONE).run(args);
    }

    @Bean
    public CommandLineRunner runner(ConsoleUI ui,Field field) {
        field.generate();
        return s -> ui.play(field);
    }

    @Bean
    public Field field() {
        return new Field(7,7,4);
    }
    @Bean
    public ConsoleUI consoleUI() {
        return new ConsoleUI();
    }

    @Bean
    public MenuUI menuUI(){
        return new MenuUI();
    }

    @Bean
    public ScoreService scoreService() {
        return new ScoreServiceRestClient();
    }

    @Bean
    public CommentService commentService(){
        return new CommentServiceRestClient();
    }

    @Bean
    public RatingService ratingService(){
        return new RatingServiceRestClient();
    }

    @Bean
    public GameInfoService gameInfoService(){
        return new GameInfoServiceRestClient();
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
