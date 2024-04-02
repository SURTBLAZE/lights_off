package lightsoff.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lightsoff.entity.GameInfo;


@Transactional
public class GameInfoServiceJPA implements GameInfoService{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addGameInfo(GameInfo gameInfo) throws GameInfoException {
        entityManager.persist(gameInfo);
    }

    @Override
    public String getGameInfo(String game) throws GameInfoException {
        return (String) entityManager.createNamedQuery("GameInfo.getGameInfo")
                .setParameter("game",game).getSingleResult();
    }

    @Override
    public void reset() throws GameInfoException {
        entityManager.createNamedQuery("GameInfo.resetGameInfo").executeUpdate();
    }
}
