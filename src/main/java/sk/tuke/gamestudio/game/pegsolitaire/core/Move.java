package sk.tuke.gamestudio.game.pegsolitaire.core;

public class Move {
    private int fromRow;
    private int fromCol;
    private int toRow;
    private int toCol;

    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    public int getFromCol() {return fromCol;
    }
    public void setFromCol(int fromCol) {
        this.fromCol = fromCol;
    }

    public int getFromRow() {return fromRow;
    }
    public void setFromRow(int fromRow) {
        this.fromRow = fromRow;
    }

    public int getToCol() {return toCol;
    }
    public void setToCol(int toCol) {
        this.toCol = toCol;
    }

    public int getToRow() {return toRow;
    }
    public void setToRow(int toRow) {
        this.toRow = toRow;
    }


    public boolean isValid(GameBoard board) {
        // TODO: Реалізувати перевірку правильності ходу
        return false;
    }
}
