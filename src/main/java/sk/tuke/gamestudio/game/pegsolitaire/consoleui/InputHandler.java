package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.game.pegsolitaire.core.Move;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {
    private final Scanner scanner;

    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public Move readMove() {
        // TODO – отримує введення користувача та перетворює його на хід \ prijíma vstup od používateľa a konvertuje ho na pohyb
        Pattern pattern = Pattern.compile("(\\d)(\\d) (\\d)(\\d)|E");
        while (true) {
            System.out.print("Enter move (fromRow|fromCol toRow|toCol) or E/e to exit: ");
            String input = scanner.nextLine().trim().toUpperCase();
            if (input.equals("E")) { // exit
                return null;
            }
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                int fromRow = Integer.parseInt(matcher.group(1));
                int fromCol = Integer.parseInt(matcher.group(2));
                int toRow = Integer.parseInt(matcher.group(3));
                int toCol = Integer.parseInt(matcher.group(4));
                return new Move(fromRow, fromCol, toRow, toCol);
            } else {
                System.out.println("Input format is incorrect. Please enter four digits separated by spaces (e.g., '23 13') or 'E/e' to exit.");
            }
        }
    }
}
