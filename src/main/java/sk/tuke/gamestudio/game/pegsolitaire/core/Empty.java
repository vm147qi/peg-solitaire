package sk.tuke.gamestudio.game.pegsolitaire.core;

public class Empty extends Tile {
    public Empty() {
        super(TileState.EMPTY);
    }

    @Override
    public String toString() {
        return "○";
    }
}
