package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.game.pegsolitaire.core.*;
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
    private BoardShape boardShape;

    public ConsoleUI() {
        inputHandler = new InputHandler();
        renderer = new Renderer();
        scanner = new Scanner(System.in);
        commentService = new CommentServiceJDBC();
        ratingService = new RatingServiceJDBC();
        scoreService = new ScoreServiceJDBC();
    }

    public void startGame() {
        GameTitle.display();
        showTopScores();
        System.out.println("  ");
        askForRules();
        System.out.println("  ");
        askForBoardShape();
        GameBoard board = new GameBoard(boardShape);
        play(board);
    }

    public void play(GameBoard board) {
        //  TODO: запускає гру та керує ігровим процесом. \ spustí hru a ovláda hranie.
        long startTime = System.currentTimeMillis();
        int moveCount = 0;

        while (!board.isGameOver()) {
            renderer.drawBoard(board);
            Move move = inputHandler.readMove(board);
            if (move == null) {
                System.out.println("Exiting game...");
                return;
            }

            if (move == InputHandler.UNDO_MOVE) {
                if (board.undoMove()) {
                    System.out.println("Last move undone.");
                } else {
                    System.out.println("No moves to undo.");
                }
                continue;
            }

            if (board.applyMove(move)) {
                moveCount++;
            } else {
                System.out.println("Invalid move! Try another move.");
            }
        }
        renderer.drawBoard(board);
        showGameStatistics(board, startTime, moveCount);
        if (board.getState() == GameState.WON) {
            System.out.println("\u001B[31mCongratulations! You won!\u001B[0m");
            scoreSubmission(board);
        } else {
            System.out.println("\u001B[31mGame Over! No valid moves.\u001B[0m");
            scoreSubmission(board);
        }
        gameFeedback();
        startNewGame();
    }

    private void startNewGame() {
        System.out.print("Do you want to start a new game (Y/N)? ");
        System.out.println("  ");
        String response = scanner.nextLine().trim().toUpperCase();
        if (response.equals("Y")) {
            askForBoardShape();
            GameBoard board = new GameBoard(boardShape);
            play(board);
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
            int rating;
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

    private void askForRules() {
        String respons;
        do {
            System.out.print("Do you want to see the rules of the game (Y/N)? ");
            respons = scanner.nextLine().trim().toUpperCase();
            if (respons.equals("Y")) {
                GameRules.displayRules();
                break;
            } else if (respons.equals("N")) {
                break;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        } while (true);
    }

    private void askForBoardShape() {
        while (true) {
            System.out.println("Choose the board shape(More difficult level = Higher score):");
            System.out.println("1. Triangle(Easy)");
            System.out.println("2. Classic(Medium)");
            System.out.println("3. European(Hard)");
            System.out.println("  ");
            System.out.print("Enter your choice (1/2/3) -> ");
            String choice = scanner.nextLine().trim();
            if (choice.isEmpty()) {
                System.out.println("Input cannot be empty! Try again.");
                continue;
            }
            switch (choice) {
                case "1":
                    boardShape = BoardShape.TRIANGULAR;
                    return;
                case "2":
                    boardShape = BoardShape.CLASSIC;
                    return;
                case "3":
                    boardShape = BoardShape.EUROPEAN;
                    return;
                default:
                    System.out.println("Invalid choice. Please enter 1, 2, or 3.");
            }
        }
    }

    private void showGameStatistics(GameBoard board, long startTime, int moveCount) {
        long endTime = System.currentTimeMillis();
        long timeSpent = (endTime - startTime) / 1000;
        int remainingPegs = countRemainingPegs(board);

        System.out.println("\n--- GAME STATISTICS ---");
        System.out.println("Total moves: " + moveCount);
        System.out.println("Time spent: " + timeSpent + " seconds");
        System.out.println("Remaining pegs: " + remainingPegs);
        System.out.println("  ");
    }

    private int countRemainingPegs(GameBoard board) {
        int count = 0;
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                Tile tile = board.getTile(row, col);
                if (tile != null && tile.getState() == TileState.PEG) {
                    count++;
                }
            }
        }
        return count;
    }
}
