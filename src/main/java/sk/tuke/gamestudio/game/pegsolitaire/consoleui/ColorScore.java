package sk.tuke.gamestudio.game.pegsolitaire.consoleui;

public class ColorScore {
    private static final String[] COLORS = {
            "\u001B[31m",
            "\u001B[32m",
            "\u001B[33m",
            "\u001B[34m",
            "\u001B[35m",
            "\u001B[36m",
            "\u001B[37m",
    };
    public static String getColoredTitle(String title) {
        StringBuilder coloredTitle = new StringBuilder();
        for (int i = 0; i < title.length(); i++) {
            // Výber farby z poľa po jednej
            coloredTitle.append(COLORS[i % COLORS.length])
                    .append(title.charAt(i))
                    .append("\u001B[0m");
        }
        return coloredTitle.toString();
    }
}
