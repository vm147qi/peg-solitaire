package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import java.util.Scanner;

public class InputHandler {
    private Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public String readMove() {
        // TODO Move – отримує введення користувача та перетворює його на хід
        return scanner.nextLine();
    }
}
