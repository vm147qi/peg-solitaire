package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreServiceTest {

    private ScoreService scoreService;

    @BeforeEach
    public void setUp() {
        scoreService = new ScoreServiceJDBC();
    }

    @Test
    public void reset() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("mines").size());
    }

}
