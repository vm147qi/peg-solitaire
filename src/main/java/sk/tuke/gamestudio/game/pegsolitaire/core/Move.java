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
        //kontrola správneho kurzu
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
        return (deltaRow == 2 && deltaCol == 0) ||  //Vertikálny ťah (nahor alebo nadol)
                (deltaCol == 2 && deltaRow == 0) || //Vodorovný ťah (vľavo alebo vpravo)
                (deltaRow == 2 && deltaCol == 2);   //Diagonálny ťah
    }
}
