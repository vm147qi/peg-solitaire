package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.game.pegsolitaire.core.GameBoard;
import sk.tuke.gamestudio.game.pegsolitaire.core.NonPlayable;
import sk.tuke.gamestudio.game.pegsolitaire.core.Tile;
import sk.tuke.gamestudio.game.pegsolitaire.core.TileState;

public class Renderer {
  public void drawBoard(GameBoard board) {
      //  číslo stĺpca
      System.out.print("   ");
      for (int col = 0; col < board.getWidth(); col++) {
          System.out.printf(" %d  ", col);
      }
      System.out.println();

      // Horná hranica
      System.out.print("   ");
      for (int col = 0; col < board.getWidth(); col++) {
          System.out.print("────");
      }
      System.out.println();

      for (int row = 0; row < board.getHeight(); row++) {
          // číslo riadku
          System.out.printf("%2d │", row);
          for (int col = 0; col < board.getWidth(); col++) {
              Tile tile = board.getTile(row, col);
              if (tile == null || tile instanceof NonPlayable) {
                  System.out.print("   │");
              } else {
                  System.out.printf(" %s │", tile);
              }
          }
          System.out.println();

          // Čiara medzi riadkami
          System.out.print("   ");
          for (int col = 0; col < board.getWidth(); col++) {
              System.out.print("────");
          }
          System.out.println();
      }
  }

}
