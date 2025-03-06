package sk.tuke.gamestudio.game.pegsolitaire;

import sk.tuke.gamestudio.game.pegsolitaire.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.pegsolitaire.core.GameBoard;
import sk.tuke.gamestudio.game.pegsolitaire.core.GameState;
import sk.tuke.gamestudio.game.pegsolitaire.core.Tile;

public class PegSolitaireMain {
    public static void main(String[] args) {

        int width = 7;
        int height = 7;

        Tile[][] tiles = new Tile[height][width];

        GameState state = GameState.PLAYING;

        GameBoard board = new GameBoard(width, height, tiles, state);

        ConsoleUI consoleUI = new ConsoleUI();

        consoleUI.play(board);
    }
}
