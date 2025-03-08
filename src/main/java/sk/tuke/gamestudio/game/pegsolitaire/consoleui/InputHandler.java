package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.game.pegsolitaire.core.Move;

import java.util.Scanner;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public Move readMove() {
        // TODO – отримує введення користувача та перетворює його на хід \ prijíma vstup od používateľa a konvertuje ho na pohyb
        System.out.print("Enter move fromRow fromCol toRow toCol: ");
        int fromRow = scanner.nextInt();
        int fromCol = scanner.nextInt();
        int toRow = scanner.nextInt();
        int toCol = scanner.nextInt();
        return new Move(fromRow, fromCol, toRow, toCol);
    }
}
