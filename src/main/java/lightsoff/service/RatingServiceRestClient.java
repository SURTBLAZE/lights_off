package lightsoff.service;

import lightsoff.entity.Rating;
import lightsoff.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class RatingServiceRestClient implements RatingService{
    private final String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;
    //private RestTemplate restTemplate = new RestTemplate();

    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getRating(String gameName,String player) {
        return restTemplate.getForEntity(url + "/" + gameName + "/" + player, int.class).getBody();
    }

    @Override
    public int getAverageRating(String game){
        return restTemplate.getForEntity(url + "/" + game,int.class).getBody();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
