package lightsoff.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lightsoff.entity.Rating;

import java.util.Objects;

@Transactional
public class RatingServiceJPA implements RatingService{
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public void setRating(Rating rating) throws RatingException {
        if(getRating(rating.getGame(),rating.getPlayer()) == -1) {
            entityManager.persist(rating);
        }
        else{
            entityManager.createNamedQuery("Rating.updateRating")
                    .setParameter("rating",rating.getRating())
                    .setParameter("game",rating.getGame())
                    .setParameter("player",rating.getPlayer())
                    .executeUpdate();
        }
    }

    @Override
    public int getAverageRating(String game) throws RatingException {
        Object obj = entityManager.createNamedQuery("Rating.getAverageRating")
                .setParameter("game",game)
                .getSingleResult();
        if(obj != null){
            return (int)(double) obj;
        }
        return -1;
    }

    @Override
    public int getRating(String game, String player) throws RatingException {
        Object obj;
        try {
            obj = entityManager.createNamedQuery("Rating.getRating")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
        }
        catch (Exception e){
            return -1;
        }
        return (int)obj;
    }

    @Override
    public void reset() throws RatingException {
        entityManager.createNamedQuery("Rating.resetRatings").executeUpdate();
    }
}
