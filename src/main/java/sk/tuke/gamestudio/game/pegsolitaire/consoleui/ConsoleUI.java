package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.game.pegsolitaire.core.GameBoard;
import sk.tuke.gamestudio.game.pegsolitaire.core.GameState;
import sk.tuke.gamestudio.game.pegsolitaire.core.Move;

import java.util.Scanner;

public class ConsoleUI {
    private final InputHandler inputHandler;
    private final Renderer renderer;
    private final Scanner scanner;
    private int width, height;

    public ConsoleUI(int width, int height) {
        inputHandler = new InputHandler();
        renderer = new Renderer();
        scanner = new Scanner(System.in);
        this.width = width;
        this.height = height;
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
        } else {
            System.out.println("Game Over! No valid moves.");
        }
        startNewGame();
    }

    private void startNewGame() {
        System.out.print("Do you want to start a new game (A/N)? ");
        String response = scanner.nextLine().trim().toUpperCase();
        if (response.equals("A")) {
            play(new GameBoard(width, height));
        } else {
            System.out.println("Exiting game...");
        }
    }
}
