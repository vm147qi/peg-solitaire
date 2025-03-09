package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.game.pegsolitaire.core.GameBoard;
import sk.tuke.gamestudio.game.pegsolitaire.core.GameState;
import sk.tuke.gamestudio.game.pegsolitaire.core.Move;

public class ConsoleUI {
    private final InputHandler inputHandler;
    private final Renderer renderer;

    public ConsoleUI() {
        inputHandler = new InputHandler();
        renderer = new Renderer();
    }

    public void play(GameBoard board) {
        //  TODO: запускає гру та керує ігровим процесом. \ spustí hru a ovláda hranie.
        while (!board.isGameOver()) {
            renderer.drawBoard(board);
            Move move = inputHandler.readMove();
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
    }
}
