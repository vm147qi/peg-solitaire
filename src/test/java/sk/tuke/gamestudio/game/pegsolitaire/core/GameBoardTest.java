package sk.tuke.gamestudio.game.pegsolitaire.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameBoardTest {

    @Test
    public void testInitialBoard() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);

        assertEquals(7, board.getWidth(), "Šírka hracej dosky by mala byť 7.");
        assertEquals(7, board.getHeight(), "Výška hracej dosky by mala byť 7.");

        assertEquals(TileState.EMPTY, board.getTile(3, 3).getState(), "Stredná dlaždica by mala byť prázdna.");
        assertEquals(TileState.NON_PLAYABLE, board.getTile(0, 0).getState(), "Dlaždica (0,0) by mala byť NonPlayable.");
        assertEquals(TileState.PEG, board.getTile(2, 2).getState(), "Dlaždica (2,2) by mala obsahovať Peg.");
    }

    @Test
    public void testBoardGeneration() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        int pegCount = 0;

        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (board.getTile(row, col).getState() == TileState.PEG) {
                    pegCount++;
                }
            }
        }
        assertEquals(32, pegCount, "By malo byť 32 Pegs na doske.");
    }

    @Test
    public void testValidMoveApplication() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        Move move = new Move(3, 1, 3, 3);

        assertTrue(board.applyMove(move), "Pohyb by mal byť platný a úspešný.");
        assertEquals(TileState.EMPTY, board.getTile(3, 1).getState(), "Dlaždica [3,1] by mala byť prázdna.");
        assertEquals(TileState.PEG, board.getTile(3, 3).getState(), "Dlaždica [3,3] by mala obsahovať Peg.");
        assertEquals(TileState.EMPTY, board.getTile(3, 2).getState(), "Dlaždica [3,2] by mala byť prázdna.");
    }

    @Test
    public void testGameOverBeginning() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        assertFalse(board.isGameOver(), "Hra by nemala byť na začiatku ukončená.");
    }

    @Test
    public void testValidMovesList() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        assertNotNull(board.getValidMoves(), "Zoznam platných pohybov by nemal byť null.");
        assertFalse(board.getValidMoves().isEmpty(), "Na začiatku by mali byť dostupné pohyby.");
    }

    @Test
    public void testWinCondition() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                board.tiles[row][col] = new Empty();
            }
        }
        board.tiles[3][3] = new Peg();

        assertTrue(board.isGameOver(), "Hra by mala byť ukončená, keď zostane iba jeden Peg.");
        assertEquals(GameState.WON, board.getState(), "Stav hry by mal byť WON, keď zostane iba jeden Peg.");
    }

    @Test
    public void testLosingCondition() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                board.tiles[row][col] = new Empty();
            }
        }
        board.tiles[0][0] = new Peg();
        board.tiles[6][6] = new Peg();
        assertTrue(board.isGameOver(), "Hra by mala byť ukončená, keď nie sú žiadne platné pohyby.");
    }

    @Test
    public void testInvalidMove() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        Move invalidMove = new Move(3, 1, 3, 2);

        assertFalse(board.applyMove(invalidMove), "Nesprávny pohyb by mal byť zamietnutý.");
        assertEquals(TileState.PEG, board.getTile(3, 1).getState(), "Dlaždica [3,1] by mala obsahovať Peg.");
        assertEquals(TileState.PEG, board.getTile(3, 2).getState(), "Dlaždica [3,2] by mala zostať nezmenená.");
    }

    @Test
    public void testGameStateAfterNoMoves() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                board.tiles[row][col] = new Empty();
            }
        }
        board.tiles[0][0] = new Peg();
        board.tiles[6][6] = new Peg();

        assertTrue(board.isGameOver(), "Hra by mala byť ukončená, keď nie sú žiadne platné pohyby.");
        assertEquals(GameState.NO_MOVES, board.getState(), "Stav hry by mal byť NO_MOVES, keď nie sú žiadne platné pohyby.");
    }

    /// ======
    @Test
    public void testPegCountAfterMove() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        int initialPegCount = countPegs(board);
        Move move = new Move(3, 1, 3, 3);
        assertTrue(board.applyMove(move), "Pohyb by mal byť platný.");
        int newPegCount = countPegs(board);
        assertEquals(initialPegCount - 1, newPegCount, "Po vykonaní pohybu by mal byť počet Pegs o jeden menší.");
    }
    private int countPegs(GameBoard board) {
        int count = 0;
        for (int row = 0; row < board.getHeight(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                if (board.getTile(row, col).getState() == TileState.PEG) {
                    count++;
                }
            }
        }
        return count;
    }
    /// ========

    @Test
    public void testGameNotOverAfterOneMove() {
        GameBoard board = new GameBoard(BoardShape.CLASSIC);
        Move move = new Move(3, 1, 3, 3);
        assertTrue(board.applyMove(move), "Pohyb by mal byť platný.");
        assertFalse(board.isGameOver(), "Po jednom ťahu by hra nemala byť ukončená.");
    }
}
