package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private BoardShape boardShape;
    private String player;

    protected GameBoard board;

    private CommentService commentService;
    private RatingService ratingService;
    private ScoreService scoreService;

    public ConsoleUI() {
        inputHandler = new InputHandler();
        renderer = new Renderer();
        scanner = new Scanner(System.in);
    }

    @Autowired
    public void setScoreService(ScoreService scoreService) {
        this.scoreService = scoreService;
    }
    @Autowired
    public void setRatingService(RatingService ratingService) {
        this.ratingService = ratingService;
    }
    @Autowired
    public void setCommentService(CommentService commentService) {
        this.commentService = commentService;
    }


    public void startGame() {
        GameTitle.display();
        showGameInfo();
        showTopScores();
        if (askForComments()) {
            showLatestComments();
        }
        System.out.println("  ");
        askForRules();
        System.out.println("  ");
        askForBoardShape();
        this.board = new GameBoard(boardShape);
        play(board);
    }

    public void play(GameBoard board) {
        //spustí hru a ovláda hranie.
        long startTime = System.currentTimeMillis();
        int moveCount = 0;

        while (!board.isGameOver()) {
            renderer.drawBoard(board);
            Move move = inputHandler.readMove(board);
            if (move == null) {
                System.out.println("\u001B[31mYou are out of the game...\u001B[0m");
                break;
            }

            if (move == InputHandler.UNDO_MOVE) {
                if (board.undoMove()) {
                    System.out.println("Last move undone.");
                    MusicalGame.playSound("move.wav");
                } else {
                    System.out.println("No moves to undo.");
                }
                continue;
            }

            if (board.applyMove(move)) {
                moveCount++;
                MusicalGame.playSound("move.wav");
            } else {
                System.out.println("Invalid move! Try another move.");
            }
        }
        if (board.isGameOver()) {
            renderer.drawBoard(board);
            if (board.getState() == GameState.WON) {
                System.out.println("\u001B[31mCongratulations! You won!\u001B[0m");
                MusicalGame.playSound("win.wav");
            } else {
                System.out.println("\u001B[31mGame Over! No valid moves.\u001B[0m");
                MusicalGame.playSound("lose.wav");
            }
        }
        showGameStatistics(board, startTime, moveCount);
        System.out.println("  ");
        askForPlayerName();
        scoreSubmission(board);
        gameFeedback();
        startNewGame();
    }

    private void showGameInfo() {
        String gameName = "PegSolitaire";
        String developer = "Veronika Mukosii";
        String releaseDate = "March 2025";

        double averageRating = ratingService.getAverageRating(gameName);
        int voteCount = ratingService.getVoteCount(gameName);
        final String YELLOW = "\u001B[33m";
        final String RESET = "\u001B[0m";
        int fullStars = (int) averageRating;
        int emptyStars = 5 - fullStars;
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < fullStars; i++) {
            stars.append(YELLOW).append("★").append(RESET);}
        for (int i = 0; i < emptyStars; i++) {
            stars.append("☆");}

        String title = "            --- GAME INFORMATION ---";
        String coloredTitle = ColorScore.getColoredTitle(title);
        System.out.println("\n" + coloredTitle);

        System.out.println("+----------------------+----------------------------+");
        System.out.printf("| %-20s | %-26s |\n", "Game Name", gameName);
        System.out.printf("| %-20s | %-26s |\n", "Developer", developer);
        System.out.printf("| %-20s | %-26s |\n", "Release Date", releaseDate);
        System.out.printf("| %-20s | %-26s          |\n", "Rating", String.format("%s (%d Votes)", stars, voteCount));
        System.out.println("+----------------------+----------------------------+");
    }

    private void startNewGame() {
        System.out.print("Do you want to start a new game (Y/N)? :  ");
        String response = scanner.nextLine().trim().toUpperCase();
        if (response.equals("Y")) {
            askForBoardShape();
            GameBoard board = new GameBoard(boardShape);
            play(board);
        } else {
            System.out.println("Exiting game...");
        }
    }

    private void askForPlayerName() {
        System.out.print("Enter your name: ");
        player = scanner.nextLine().trim();
        while (player.isEmpty()) {
            System.out.println("Name cannot be empty! Try again.");
            System.out.print("Enter your name: ");
            player = scanner.nextLine().trim();
        }
    }

    private void scoreSubmission(GameBoard board) {
        int points = board.calculateScore();
        System.out.println("Your score: " + points);

        scoreService.addScore(new Score( "PegSolitaire", player, points, new Date()));
        showTopScores();
        System.out.println("  ");
    }

    private void gameFeedback() {
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
        String title = "    --- TOP 10 SCORES GAME PEGSOLITAIRE ---";
        String colorScore = ColorScore.getColoredTitle(title);
        System.out.println("\n" + colorScore);
        List<Score> scores = scoreService.getTopScores("PegSolitaire");
        if (scores.isEmpty()) {
            System.out.println("No scores found.");
            return;
        }
        final String YELLOW = "\u001B[33m";
        final String RESET = "\u001B[0m";
        int maxScore = scores.stream().mapToInt(Score::getPoints).max().orElse(0);
        System.out.println("+------------+--------+-------------------------------+");
        System.out.printf("| %-10s | %-6s | %-19s           |\n", "Player", "Points", "Date");
        System.out.println("+------------+--------+-------------------------------+");

        for (Score score : scores) {
            String playerName = score.getPlayer();
            String displayName = playerName;
            if (score.getPoints() == maxScore) {
                displayName = YELLOW + playerName + " ★" + RESET;
            }
            int visibleLength = playerName.length() + (score.getPoints() == maxScore ? 2 : 0);
            int padding = 10 - visibleLength;
            System.out.printf("| %s%s | %-6d | %-19s |\n",
                    displayName, " ".repeat(Math.max(0, padding)),
                    score.getPoints(),
                    score.getPlayedOn());
        }
        System.out.println("+------------+--------+-------------------------------+");
    }

    /// ////comment
    private boolean askForComments() {
        while (true) {
            System.out.print("\nWould you like to read the latest comments? (Y/N): ");
            String response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("Y")) {
                return true;
            } else if (response.equals("N")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        }
    }

    private void showLatestComments() {
        List<Comment> comments = commentService.getComments("PegSolitaire");
        if (comments.isEmpty()) {
            System.out.println("\nNo comments found.");
            return;
        }
        String title = "               --- LATEST COMMENTS ---";
        String colorScore = ColorScore.getColoredTitle(title);
        System.out.println("\n" + colorScore);
        System.out.println("+------------+---------------------------+--------------------------------+");
        System.out.printf("| %-10s | %-25s | %-19s            |\n", "Player", "Comment", "Date");
        System.out.println("+------------+---------------------------+--------------------------------+");
        int count = 0;
        for (Comment comment : comments) {
            System.out.printf("| %-10s | %-25s | %-19s  |\n",
                    comment.getPlayer(),
                    truncateComment(comment.getComment(), 25),
                    comment.getCommentedOn());
            count++;
            if (count >= 3) break;
        }
        System.out.println("+------------+---------------------------+--------------------------------+");
    }

    private String truncateComment(String comment, int maxLength) {
        if (comment.length() > maxLength) {
            return comment.substring(0, maxLength - 3) + "...";
        }
        return comment;
    }
    /// /////comment

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
            System.out.println("Choose the board shape(\u001B[31mMore difficult level = Higher score\u001B[0m):");
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

        String title = "    --- GAME STATISTICS ---" ;
        String colorScore = ColorScore.getColoredTitle(title);
        System.out.println("\n" + colorScore);
        System.out.println("+----------------+------------+");
        System.out.printf("| %-13s  | %-10d |\n", "Total moves", moveCount);
        System.out.printf("| %-13s  | %-10d |\n", "Time spent ", timeSpent);
        System.out.printf("| %-13s | %-10d |\n", "Remaining pegs", remainingPegs);
        System.out.println("+----------------+------------+");
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
