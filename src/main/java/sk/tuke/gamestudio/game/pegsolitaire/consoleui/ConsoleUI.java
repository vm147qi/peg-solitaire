package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.game.pegsolitaire.core.GameBoard;

public class ConsoleUI {
    private InputHandler inputHandler;
    private Renderer renderer;

    public ConsoleUI() {
        inputHandler = new InputHandler();
        renderer = new Renderer();
    }

    public void play(GameBoard board) {
        //  TODO запускає гру та керує ігровим процесом.
        return;
    }

    public void show(GameBoard board) {
        //  TODO виводить поточний стан гри в консоль.
        renderer.drawBoard(board);
    }

    private void handleInput(GameBoard board) {
        //  TODO  обробляє введені користувачем команди.
        return;
    }
}
