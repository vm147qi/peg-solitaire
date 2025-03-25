package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import sk.tuke.gamestudio.game.pegsolitaire.core.GameBoard;
import sk.tuke.gamestudio.game.pegsolitaire.core.Move;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InputHandler {
    private final Scanner scanner;
    public static final Move UNDO_MOVE = new Move(-1, -1, -1, -1);


    public InputHandler() {
        this.scanner = new Scanner(System.in);
    }

    public Move readMove(GameBoard board) {
        //prijíma vstup od používateľa a konvertuje ho na pohyb
        Pattern pattern = Pattern.compile("(\\d)(\\d)\\s(\\d)(\\d)|[UE]");
        while (true) {
            System.out.println(board);
            System.out.print("Enter move (fromRow|fromCol toRow|toCol), 'U' to undo, or 'E' to exit: ");
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.equals("E")) { // exit
                return null;
            }

            if (input.equals("U")) { // undo
                return UNDO_MOVE;
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
