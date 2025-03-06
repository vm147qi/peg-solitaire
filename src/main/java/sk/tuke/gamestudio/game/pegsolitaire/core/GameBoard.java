package sk.tuke.gamestudio.game.pegsolitaire.core;

import java.util.List;

public class GameBoard {
    private int width;
    private int height;
    private Tile[][] tiles;
    private GameState state;

    public GameBoard(int width, int height, Tile[][] tiles, GameState state) {
        this.width = width;
        this.height = height;
        this.tiles = tiles;
        this.state = state;
    }

    public int getHeight() {return height;
    }
    public void setHeight(int height) {this.height = height;
    }

    public GameState getState() {return state;
    }
    public void setState(GameState state) {this.state = state;
    }

    public Tile[][] getTiles() {return tiles;
    }
    public void setTiles(Tile[][] tiles) {this.tiles = tiles;
    }

    public int getWidth() {return width;
    }
    public void setWidth(int width) {this.width = width;
    }

    public boolean applyMove(Move move) {
        // TODO: застосовує хід move, змінюючи стан дошки. Повертає true, якщо хід успішний.
        return false;
    }

    public boolean isGameOver() {
        // TODO: перевіряє, чи закінчилась гра.
        return false;
    }

    public List<Move> getValidMoves() {
        // TODO: – повертає список усіх можливих ходів.
        return null;
    }

    /// ДОДАТИ В ПДФ ///
    public boolean isWithinBounds(int row, int col) {
        // TODO: Перевіряє, чи знаходяться координати в межах поля
        return false;
    }

    /// ДОДАТИ В ПДФ ///
    public boolean hasPeg(int row, int col) {
        // TODO: Перевіряє, чи вказана клітинка містить фішку
        return false;
    }
}
