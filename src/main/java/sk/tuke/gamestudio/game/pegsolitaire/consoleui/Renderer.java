package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.game.pegsolitaire.core.GameBoard;
import sk.tuke.gamestudio.game.pegsolitaire.core.TileState;

public class Renderer {

    public void drawBoard(GameBoard board) {
        // TODO малює поточний стан дошки в консолі. \ vykreslí aktuálny stav dosky v konzole.
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (board.getTile(row, col).getState() == TileState.PEG) {
                    System.out.print("P ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
