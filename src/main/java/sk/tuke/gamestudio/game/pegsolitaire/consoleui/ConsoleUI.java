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
        System.out.println("  ");
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
            scoreSubmission(board);
        } else {
            System.out.println("Game Over! No valid moves.");
            scoreSubmission(board);
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

    private void scoreSubmission(GameBoard board) {
        System.out.print("Enter your name: ");
        String player = scanner.nextLine().trim();

        if (player.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        int points = board.calculateScore();
        System.out.println("Your score: " + points);

        scoreService.addScore(new Score( "PegSolitaire", player, points, new Date()));
        showTopScores();
        System.out.println("  ");
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
            int rating = -1;
            while (true) {
                System.out.print("Enter your rating (1-5): ");
                try {
                    rating = Integer.parseInt(scanner.nextLine().trim());
                    if (rating >= 1 && rating <= 5) {
                        break;  //Ukončenie cyklu, ak je hodnotenie správne
                    } else {
                        System.out.println("Invalid rating! Please enter a number between 1-5.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a valid number."); // pre písmená
                }
            }
            ratingService.setRating(new Rating("PegSolitaire",player, rating, new Date()));
        }
    }

    private void showTopScores() {
        String title = "--- TOP 10 SCORES GAME PEGSOLITAIRE ---";
        String colorScore = ColorScore.getColoredTitle(title);
        System.out.println("\n" + colorScore);

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
