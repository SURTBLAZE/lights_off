package lightsoff.service;

import lightsoff.entity.GameInfo;
import lightsoff.entity.Score;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class GameInfoServiceRestClient implements GameInfoService{
    private final String url = "http://localhost:8080/api/gameinfo";

    @Autowired
    private RestTemplate restTemplate;
    //private RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getGameInfo(String game) {
        return restTemplate.getForEntity(url + "/" + game,String.class).getBody();
    }

    @Override
    public void addGameInfo(GameInfo gameInfo){
        throw new UnsupportedOperationException("Not supported via web service");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
