package sk.tuke.gamestudio.game.pegsolitaire.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameBoard {
    public int width;
    public int height;
    public Tile[][] tiles;
    private GameState state;
    private final BoardShape shape;
    private final Stack<Move> moveHistory;

    public GameBoard(BoardShape shape) {
        this.shape = shape;
        switch (shape) {
            case TRIANGULAR:
                width = 5;
                height = 5;
                break;
            case CLASSIC:
                width = 7;
                height = 7;
                break;
            case EUROPEAN:
                width = 9;
                height = 9;
                break;
        }
        this.moveHistory = new Stack<>();
        initializeBoard();
        this.state = GameState.PLAYING;
    }

    public int getHeight() {return height;}

    public Tile getTile(int row, int col) {
        if (isValidBorders(row, col)) {
            return tiles[row][col];
        }
        return null;
    }

    public int getWidth() {return width;}

    public GameState getState() {
        if (hasWon()) {
            return GameState.WON;
        }
        if (isGameOver()) {
            return GameState.NO_MOVES;
        }
        return GameState.PLAYING;
    }

    public boolean applyMove(Move move) {
        if (!move.isValid(this)) return false;
        // súradnice bunky, ktorú preskakujeme
        int midBoxRow = (move.getFromRow() + move.getToRow()) / 2;
        int midBoxCol = (move.getFromCol() + move.getToCol()) / 2;

        tiles[move.getFromRow()][move.getFromCol()] = new Empty(); //odstráni počiatočnu figurku
        tiles[midBoxRow][midBoxCol] = new Empty(); // odstráni skočenu figurku
        tiles[move.getToRow()][move.getToCol()] = new Peg(); // vytvorí nový na novom mieste.

        moveHistory.push(move);
        if (isGameOver()) state = GameState.NO_MOVES;
        return true;
    }

    public boolean undoMove() {
        if (moveHistory.isEmpty()) return false;
        Move lastMove = moveHistory.pop();

        int midBoxRow = (lastMove.getFromRow() + lastMove.getToRow()) / 2;
        int midBoxCol = (lastMove.getFromCol() + lastMove.getToCol()) / 2;

        tiles[lastMove.getFromRow()][lastMove.getFromCol()] = new Peg();
        tiles[midBoxRow][midBoxCol] = new Peg();
        tiles[lastMove.getToRow()][lastMove.getToCol()] = new Empty();
        return true;
    }

    ///
    private boolean hasWon() {
        return getRemainingPegs() == 1;
    }

    public boolean isGameOver() {
        return getRemainingPegs() == 1 || getValidMoves().isEmpty();
    }

    private int getRemainingPegs() {
        int countPeg = 0;
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                if (tile != null && tile.getState() == TileState.PEG) countPeg++;
            }
        }
        return countPeg;
    }
    ///

    public List<Move> getValidMoves() {
        //vráti zoznam všetkých možných ťahov.
        List<Move> moves = new ArrayList<>();
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (!isPlayableCell(row, col)) continue;
                addValidMove(moves, row, col, row + 2, col);
                addValidMove(moves, row, col, row - 2, col);
                addValidMove(moves, row, col, row, col + 2);
                addValidMove(moves, row, col, row, col - 2);
                if (shape == BoardShape.TRIANGULAR) {
                    addValidMove(moves, row, col, row + 2, col + 2);
                    addValidMove(moves, row, col, row - 2, col - 2);
                }
            }
        }
        return moves;
    }
    private void addValidMove(List<Move> moves, int fromRow, int fromCol, int toRow, int toCol) {
        if (isValidMove(fromRow, fromCol, toRow, toCol)) {
            moves.add(new Move(fromRow, fromCol, toRow, toCol));
        }
    }

    private boolean isPlayableCell(int row, int col) {
        if (shape == BoardShape.TRIANGULAR) {
            return col <= row;
        }
        return true;
    }

    public boolean isValidBorders(int row, int col) {
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        return new Move(fromRow, fromCol, toRow, toCol).isValid(this);
    }

    private void initializeBoard() {
        //Inicializuje hraciu plochu tým, že ju zaplní figúrkami okrem centrálneho štvorca.
        tiles = new Tile[height][width];
        switch (shape) {
            case CLASSIC:
                initializeClassicBoard();
                break;
            case EUROPEAN:
                initializeEuropeanBoard();
                break;
            case TRIANGULAR:
                initializeTriangularBoard();
                break;
        }
    }

    private void initializeClassicBoard() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if ((row < 2 || row > 4) && (col < 2 || col > 4)) {
                    tiles[row][col] = new NonPlayable();
                } else {
                    tiles[row][col] = new Peg();
                }
            }
        }
        tiles[3][3] = new Empty();//centrum
    }

    private void initializeEuropeanBoard() {
        int center = 4;
        int radius = 4;

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int distSquared = (row - center) * (row - center) + (col - center) * (col - center);
                if (distSquared <= radius * radius) {
                    tiles[row][col] = new Peg();
                } else {
                    tiles[row][col] = new NonPlayable();
                }
            }
        }
        tiles[center][center] = new Empty();
    }

    private void initializeTriangularBoard() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (col <= row) {
                    tiles[row][col] = new Peg();
                } else {
                    tiles[row][col] = new NonPlayable();
                }
            }
        }
        tiles[0][0] = new Empty();
    }

    public int calculateScore() {
        int score = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Tile tile = tiles[row][col];
                if (tile != null && tile.getState() == TileState.EMPTY) {
                    score++;
                }
            }
        }
        return score;
    }

    @Override
    public String toString() {
        return String.format("GameBoard [%s] (%dx%d) - State: %s", shape, width, height, state);
    }

}