package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Score;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreServiceTest {

    private ScoreService scoreService;

    @BeforeEach
    public void setUp() {
        scoreService = new ScoreServiceJDBC();
    }

    @Test
    public void testAddScore() {
        Score score = new Score("pegSolitaire", "player1", 50, new Date());
        scoreService.addScore(score);

        List<Score> scores = scoreService.getTopScores("pegSolitaire");
        assertEquals(1, scores.size());
        assertEquals("player1", scores.get(0).getPlayer());
        assertEquals(50, scores.get(0).getPoints());
    }

    @Test
    public void testGetTopScores() {
        scoreService.addScore(new Score("PegSolitaire", "player1", 30, new Date()));
        scoreService.addScore(new Score("PegSolitaire", "player2", 40, new Date())); //
        scoreService.addScore(new Score("PegSolitaire", "player3", 20, new Date()));

        List<Score> scores = scoreService.getTopScores("PegSolitaire");
        assertEquals(3, scores.size());
        assertEquals("player2", scores.get(0).getPlayer());
    }

    @Test
    public void reset() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("pegsolitaire").size());
    }

}
