package lightsoff.server.webservice;

import lightsoff.service.GameInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gameinfo")
public class GameInfoServiceRest {

    @Autowired
    private GameInfoService gameInfoService;

    @GetMapping("/{game}")
    public String getGameInfo(@PathVariable String game){
        return gameInfoService.getGameInfo(game);
    }
}
