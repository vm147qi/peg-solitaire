package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.pegsolitaire.core.GameBoard;
import sk.tuke.gamestudio.game.pegsolitaire.core.GameState;
import sk.tuke.gamestudio.game.pegsolitaire.core.Move;
import sk.tuke.gamestudio.service.*;

import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private final InputHandler inputHandler;
    private final Renderer renderer;
    private final Scanner scanner;
    private final CommentService commentService;
    private final RatingService ratingService;
    private final ScoreService scoreService;
    private int width, height;

    public ConsoleUI(int width, int height) {
        inputHandler = new InputHandler();
        renderer = new Renderer();
        scanner = new Scanner(System.in);
        commentService = new CommentServiceJDBC();
        ratingService = new RatingServiceJDBC();
        scoreService = new ScoreServiceJDBC();
        this.width = width;
        this.height = height;

        GameTitle.display();
        showTopScores();
    }

    public void play(GameBoard board) {
        //  TODO: запускає гру та керує ігровим процесом. \ spustí hru a ovláda hranie.
        while (!board.isGameOver()) {
            renderer.drawBoard(board);
            Move move = inputHandler.readMove();
            if (move == null) {
                System.out.println("Exiting game...");
                return;
            }
            if (!board.applyMove(move)) {
                System.out.println("Invalid move! Try another move.");
            }
        }
        renderer.drawBoard(board);
        if (board.getState() == GameState.WON) {
            System.out.println("Congratulations! You won!");
            scoreSubmission();
        } else {
            System.out.println("Game Over! No valid moves.");
        }
        gameFeedback();
        startNewGame();
    }

    private void startNewGame() {
        System.out.print("Do you want to start a new game (Y/N)? ");
        String response = scanner.nextLine().trim().toUpperCase();
        if (response.equals("Y")) {
            play(new GameBoard(width, height));
        } else {
            System.out.println("Exiting game...");
        }
    }

    private void scoreSubmission() {
        System.out.print("Enter your name: ");
        String player = scanner.nextLine().trim();

        if (player.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }
        System.out.print("Enter your score: ");
        int points = Integer.parseInt(scanner.nextLine().trim());

        if (points < 0) {
            System.out.println("Score cannot be negative (-10)!");
            return;
        }
        scoreService.addScore(new Score( "PegSolitaire", player, points, new Date()));
        showTopScores();
    }

    private void gameFeedback() {
        System.out.print("Enter your name: ");
        String player = scanner.nextLine().trim();

        System.out.print("Would you like to leave a comment? (Y/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.print("Enter your comment: ");
            String text = scanner.nextLine().trim();
            commentService.addComment(new Comment("PegSolitaire",player, text, new Date()));
        }

        System.out.print("Would you like to rate the game (1-5)? (Y/N): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.print("Enter your rating (1-5): ");
            int rating = Integer.parseInt(scanner.nextLine().trim());
            ratingService.setRating(new Rating("PegSolitaire",player, rating, new Date()));
        }
    }

    private void showTopScores() {
        System.out.println("\n--- TOP 10 SCORES GAME PEGSOLITAIRE ---");
        List<Score> scores = scoreService.getTopScores("PegSolitaire");

        if (scores.isEmpty()) {
            System.out.println("No scores found.");
        } else {
            for (Score score : scores) {
                System.out.printf("%s - %d points (%s)\n", score.getPlayer(), score.getPoints(), score.getPlayedOn());
            }
        }
    }

}
