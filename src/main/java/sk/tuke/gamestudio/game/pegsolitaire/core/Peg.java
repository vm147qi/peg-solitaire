package sk.tuke.gamestudio.game.pegsolitaire.core;

public class Peg extends Tile {
    public Peg() {
        super(TileState.PEG);
    }

   /* @Override
    public String toString() {
        return "B";
    }*/
    @Override
    public String toString() {
        return "\u001B[36m B\u001B[0m";
    }
}
