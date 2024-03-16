package service;

import lightsoff.service.RatingService;
import lightsoff.service.RatingServiceJDBC;
import org.junit.Test;
import lightsoff.entity.Rating;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.Assert.assertEquals;
public class RatingServiceTest {
    private RatingService ratingService = new RatingServiceJDBC();

    @Test
    public void reset(){
        var date = new Date();
        var rating = new Rating("Lights off","Valeri",5,new Timestamp(date.getTime()));
        ratingService.setRating(rating);
        ratingService.reset();
        assertEquals(-1,ratingService.getRating("Lights off","Valeri"));
    }

    @Test
    public void setRating(){
        var date = new Date();

        ratingService.reset();
        ratingService.setRating(new Rating("Lights off","Valeri",5,new Timestamp(date.getTime())));
        assertEquals(5, ratingService.getRating("Lights off","Valeri"));
        ratingService.setRating(new Rating("Lights off","Valeri",2,new Timestamp(date.getTime())));
        assertEquals(2, ratingService.getRating("Lights off","Valeri"));
    }

    @Test
    public void getAverageRating(){
        var date = new Date();

        ratingService.reset();
        ratingService.setRating(new Rating("Lights off","Valeri",5,new Timestamp(date.getTime())));
        ratingService.setRating(new Rating("Lights off","Jaroslav",3,new Timestamp(date.getTime())));

        assertEquals(4,ratingService.getAverageRating("Lights off"));
    }

    @Test
    public void getRating(){
        var date = new Date();

        ratingService.reset();
        ratingService.setRating(new Rating("Lights off","Valeri",5,new Timestamp(date.getTime())));

        assertEquals(5, ratingService.getRating("Lights off","Valeri"));
        assertEquals(-1, ratingService.getRating("Minecraft", "Jaroslav"));
    }
}
