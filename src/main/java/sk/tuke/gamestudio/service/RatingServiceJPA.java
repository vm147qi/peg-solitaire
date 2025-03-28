package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
public class RatingServiceJPA implements RatingService {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void setRating(Rating rating) {
        entityManager.persist(rating);
    }

    @Override
    public int getAverageRating(String game) {
        Object result = entityManager.createQuery(
                        "SELECT AVG(r.rating) FROM Rating r WHERE r.game = :game")
                .setParameter("game", game)
                .getSingleResult();
        return result != null ? ((Number) result).intValue() : 0;
    }

    @Override
    public int getVoteCount(String game) {
        Object result = entityManager.createQuery(
                        "SELECT COUNT(r) FROM Rating r WHERE r.game = :game")
                .setParameter("game", game)
                .getSingleResult();
        return result != null ? ((Number) result).intValue() : 0;
    }

    @Override
    public int getRating(String game, String player) {
        Object result = null;
        try {
            result = entityManager.createQuery(
                            "SELECT r.rating FROM Rating r WHERE r.game = :game AND r.player = :player")
                    .setParameter("game", game)
                    .setParameter("player", player)
                    .getSingleResult();
        } catch (Exception e) {
            return 0;
        }

        return result != null ? ((Number) result).intValue() : 0;
    }

    @Override
    public void reset() {
        entityManager.createQuery("DELETE FROM Rating").executeUpdate();
    }
}
