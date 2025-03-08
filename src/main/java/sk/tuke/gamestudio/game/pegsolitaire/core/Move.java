package sk.tuke.gamestudio.game.pegsolitaire.core;

public class Move {
    public int fromRow;
    public int fromCol;
    public int toRow;
    public int toCol;

    public Move(int fromRow, int fromCol, int toRow, int toCol) {
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
    }

    public int getFromCol() {return fromCol;}

    public int getFromRow() {return fromRow;}

    public int getToCol() {return toCol;}

    public int getToRow() {return toRow;}

    public boolean isValid(GameBoard board) {
        // TODO: Реалізувати перевірку правильності ходу / Vykonajte kontrolu správneho kurzu
        if (!board.isValidBorders(fromRow, fromCol) || !board.isValidBorders(toRow, toCol)) {
            return false;
        }

        if (board.getTile(fromRow, fromCol).getState() != TileState.PEG) {
            return false;
        }

        int midBoxRow = (fromRow + toRow) / 2;
        int midBoxCol = (fromCol + toCol) / 2;

        if (board.getTile(midBoxRow, midBoxCol).getState() != TileState.PEG) {
            return false;
        }

        if (board.getTile(toRow, toCol).getState() != TileState.EMPTY) {
            return false;
        }

        int deltaRow = Math.abs(fromRow - toRow);
        int deltaCol = Math.abs(fromCol - toCol);
        /// Хід дозволяється, якщо він на 2 клітинки вперед-назад по вертикалі або горизонталі.
        /// Ťah je povolený, ak je o 2 políčka vertikálne alebo horizontálne tam a späť.
        return (deltaRow == 2 && deltaCol == 0) ||  // Вертикальний хід (вгору або вниз) \ Vertikálny ťah (nahor alebo nadol)
                (deltaCol == 2 && deltaRow == 0) || // Горизонтальний хід (вліво або вправо) \ Vodorovný ťah (vľavo alebo vpravo)
                (deltaRow == 2 && deltaCol == 2);   // Діагональний хід (якщо правила дозволяють) \ Diagonálny ťah (ak to pravidlá umožňujú)
    }
}
