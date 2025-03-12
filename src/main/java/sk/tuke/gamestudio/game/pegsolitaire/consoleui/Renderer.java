package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.game.pegsolitaire.core.GameBoard;
import sk.tuke.gamestudio.game.pegsolitaire.core.Tile;
import sk.tuke.gamestudio.game.pegsolitaire.core.TileState;

public class Renderer {
   public void drawBoard(GameBoard board) {
       // TODO малює поточний стан дошки в консолі. \ vykreslí aktuálny stav dosky v konzole.

       //  číslo stĺpca
       System.out.print("   ");
       for (int col = 0; col < board.getWidth(); col++) {
           System.out.printf("%2d", col);
       }
       System.out.println();
       // číslo riadku
       for (int row = 0; row < board.getHeight(); row++) {
           System.out.printf("%2d ", row);

           for (int col = 0; col < board.getWidth(); col++) {
               Tile tile = board.getTile(row, col);
               System.out.printf("%2s", tile);  //tile.toString()
           }
           System.out.println();
       }
   }

}
