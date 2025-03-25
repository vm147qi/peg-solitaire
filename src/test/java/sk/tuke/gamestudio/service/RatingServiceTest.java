package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Rating;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class RatingServiceTest {
    private RatingServiceJDBC ratingService;

    @BeforeEach
    void setUp() {
        ratingService = new RatingServiceJDBC();
    }

    @Test
    void testSetRating() {
        Rating rating = new Rating("game1", "player1", 5, new java.util.Date());
        try {
            ratingService.setRating(rating);
        } catch (RatingException e) {
            fail("Expected no exception, but got: " + e.getMessage());
        }
    }

    @Test
    void testGetAverageRating() {
        try {
            int averageRating = ratingService.getAverageRating("game1");
            assertEquals(0, averageRating);
        } catch (RatingException e) {
            fail("Expected no exception, but got: " + e.getMessage());
        }
    }

    @Test
    void testGetVoteCount() {
        try {
            assertEquals(0, ratingService.getVoteCount("game1"), "Vote count should be 0 initially");

            ratingService.setRating(new Rating("game1", "player1", 5, new java.util.Date()));
            assertEquals(1, ratingService.getVoteCount("game1"), "Vote count should be 1 after one rating");

            ratingService.setRating(new Rating("game1", "player2", 4, new java.util.Date()));
            assertEquals(2, ratingService.getVoteCount("game1"), "Vote count should be 2 after two ratings");
        } catch (RatingException e) {
            fail("Expected no exception, but got: " + e.getMessage());
        }
    }

    @Test
    void testGetRatingForPlayer() {
        try {
            int playerRating = ratingService.getRating("game1", "player1");
            assertEquals(5, playerRating);
        } catch (RatingException e) {
            fail("Expected no exception, but got: " + e.getMessage());
        }
    }

    @Test
    void testResetRatings() {
        try {
            ratingService.reset();
        } catch (RatingException e) {
            fail("Expected no exception, but got: " + e.getMessage());
        }
    }
}
