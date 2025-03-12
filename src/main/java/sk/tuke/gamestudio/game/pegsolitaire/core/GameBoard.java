package sk.tuke.gamestudio.game.pegsolitaire.core;

import java.util.ArrayList;
import java.util.List;

public class GameBoard {
    public int width;
    public int height;
    public Tile[][] tiles;
    private GameState state;

    public GameBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];
        initializeBoard();
        this.state = GameState.PLAYING;
    }

    public int getHeight() {return height;}

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public int getWidth() {return width;}

    public GameState getState() {
        if (hasWon()) {
            state = GameState.WON;
        } else if (isGameOver()) {
            state = GameState.NO_MOVES;
        } else {
            state = GameState.PLAYING;
        }
        return state;
    }


    public boolean applyMove(Move move) {
        // TODO: застосовує хід move, змінюючи стан дошки. Повертає true, якщо хід успішний.
        // TODO: použije ťah, čím sa zmení stav hracej plochy. Vracia true, ak je ťah úspešný.
        if (!move.isValid(this))
            return false;

        // кординати клітинки через яку перестрибуєм / súradnice bunky, ktorú preskakujeme
        int midBoxRow = (move.getFromRow() + move.getToRow()) / 2;
        int midBoxCol = (move.getFromCol() + move.getToCol()) / 2;

        tiles[move.getFromRow()][move.getFromCol()] = new Empty(); //odstráni počiatočnu figurku
        tiles[midBoxRow][midBoxCol] = new Empty(); // odstráni skočenu figurku
        tiles[move.getToRow()][move.getToCol()] = new Peg(); // vytvorí nový na novom mieste.

        if (isGameOver()) {
            state = GameState.NO_MOVES;
        }
        return true;
    }

    public boolean isGameOver() {
        // TODO: перевіряє чи закінчилась гра. \ skontroluje, či sa hra skončila.
        int countPeg = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (tiles[row][col].getState() == TileState.PEG) {
                    countPeg++;
                }
            }
        }
        return countPeg == 1 || getValidMoves().isEmpty();
    }

    public List<Move> getValidMoves() {
        // TODO: – повертає список усіх можливих ходів. \ vráti zoznam všetkých možných ťahov.
        List<Move> moves = new ArrayList<>();
        //  int[] directions = {-2, 2};
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // všetky možné smery cesty
                if (isValidMove(row, col, row + 2, col)) {
                    moves.add(new Move(row, col, row + 2, col));
                }
                if (isValidMove(row, col, row - 2, col)) {
                    moves.add(new Move(row, col, row - 2, col));
                }
                if (isValidMove(row, col, row, col + 2)) {
                    moves.add(new Move(row, col, row, col + 2));
                }
                if (isValidMove(row, col, row, col - 2)) {
                    moves.add(new Move(row, col, row, col - 2));
                }
                if (isValidMove(row, col, row + 2, col + 2)) {
                    moves.add(new Move(row, col, row + 2, col + 2));
                }
                if (isValidMove(row, col, row - 2, col - 2)) {
                    moves.add(new Move(row, col, row - 2, col - 2));
                }
            }
        }
        return moves;
    }


    /// ДОДАТИ В ПДФ ///
    public boolean isValidBorders(int row, int col) {
        // TODO: Перевіряє, чи знаходяться координати в межах поля \ Skontroluje, či sa súradnice nachádzajú v poli
        return row >= 0 && row < height && col >= 0 && col < width;
    }

    /// ДОДАТИ В ПДФ ///
    private boolean isValidMove(int fromRow, int fromCol, int toRow, int toCol) {
        // TODO: Перевіряє, чи можливий конкретний хід \ Skontroluje, či je možné vykonať konkrétny ťah.
        return new Move(fromRow, fromCol, toRow, toCol).isValid(this);
    }

    /// ДОДАТИ В ПДФ ///
    private void initializeBoard() {
        // TODO: Ініціалізує дошку, заповнюючи її фішками, крім центральної клітинки
        // TODO: Inicializuje hraciu plochu tým, že ju zaplní figúrkami okrem centrálneho štvorca.
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                tiles[row][col] = new Peg();
            }
        }
        tiles[height / 2][width / 2] = new Empty(); // Centrum
    }


    /// ДОДАТИ В ПДФ ///
    private boolean hasWon() {
        /// TODO: Метод для перевірки виграшу \ Metóda overovania výhier
        int countPeg = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (tiles[row][col].getState() == TileState.PEG) {
                    countPeg++;
                }
            }
        }
        return countPeg == 1;
    }

    public int calculateScore() {
        int score = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (tiles[row][col].getState() == TileState.EMPTY) {
                    score++;
                }
            }
        }
        return score;
    }
}
