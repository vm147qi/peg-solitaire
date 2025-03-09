package sk.tuke.gamestudio.game.pegsolitaire;

import sk.tuke.gamestudio.game.pegsolitaire.consoleui.ConsoleUI;
import sk.tuke.gamestudio.game.pegsolitaire.core.*;

public class PegSolitaireMain {
    public static void main(String[] args) {

        int width = 4, height = 4;

        GameBoard board = new GameBoard(width, height);

        ConsoleUI ui = new ConsoleUI(width, height);

        ui.play(board);
    }
}
