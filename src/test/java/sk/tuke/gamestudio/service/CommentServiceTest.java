package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Comment;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CommentServiceTest {
    private CommentService commentService;

    @BeforeEach
    public void setUp() {
        commentService = new CommentServiceJDBC();
        commentService.reset();
    }

    @Test
    public void testAddComment() {
        Comment comment = new Comment("Tetris", "Alice", "Great game!", new Date());
        commentService.addComment(comment);

        List<Comment> comments = commentService.getComments("Tetris");
        assertEquals(1, comments.size());
        assertEquals("Tetris", comments.get(0).getGame());
        assertEquals("Alice", comments.get(0).getPlayer());
        assertEquals("Great game!", comments.get(0).getComment());
    }

    @Test
    void testGetComments() {
        CommentServiceJDBC commentService = new CommentServiceJDBC();
        commentService.addComment(new Comment("Game1", "Alice", "Great game!", new Date(System.currentTimeMillis() - 1000)));
        commentService.addComment(new Comment("Game1", "Bob", "Good job!", new Date()));

        List<Comment> comments = commentService.getComments("Game1");

        assertEquals(2, comments.size());

        assertTrue(comments.stream().anyMatch(c -> c.getPlayer().equals("Alice")));
        assertTrue(comments.stream().anyMatch(c -> c.getPlayer().equals("Bob")));
    }

    @Test
    public void testReset() {
        commentService.addComment(new Comment("Tetris", "Alice", "Great game!", new Date()));
        commentService.reset();

        List<Comment> comments = commentService.getComments("Tetris");
        assertTrue(comments.isEmpty());
    }
}
