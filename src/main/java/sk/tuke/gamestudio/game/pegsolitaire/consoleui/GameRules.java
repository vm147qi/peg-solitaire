package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

import java.util.Scanner;

public class GameRules {
    public static void displayRules() {
        String title = "\u001B[31m    --- RULES OF PEG SOLITAIRE! ---\u001B[0m";

        String rules = """
               1. The game is played on a board with pegs.
               2. The goal is to remove all pegs except one.
               3. A valid move consists of jumping a peg over an adjacent peg to an empty space.
               4. The jumped peg is removed.
               5. The game ends when no valid moves are available or when only one peg remains.
            """;

        System.out.println(title);
        System.out.println(rules);
        System.out.print("Press Enter to continue...");
        new Scanner(System.in).nextLine();
    }
}
