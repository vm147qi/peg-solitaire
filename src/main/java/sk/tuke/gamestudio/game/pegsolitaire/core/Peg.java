package sk.tuke.gamestudio.game.pegsolitaire.core;

public class Peg extends Tile {
    public Peg() {
        super(TileState.PEG);
    }
    @Override
    public String toString() {
        return "\u001B[33m●\u001B[0m";
    }
}
