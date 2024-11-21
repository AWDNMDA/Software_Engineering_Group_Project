package View;
import Model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * The Display class is responsible for rendering the game interface and
 * providing visual feedback to players, such as the board, player status, and results.
 */
public class Display {
    // Text Colors
    public static final String RESET = "\u001B[0m";
    public static final String BLACK_TEXT = "\u001B[30m";
    public static final String RED_TEXT = "\u001B[31m";
    public static final String DARK_BLUE_TEXT = "\u001B[38;2;0;51;102m";
    public static final String YELLOW_TEXT = "\u001B[33m";

    // Background Colors
    public static final String BLACK_BACKGROUND = "\u001B[40m";
    public static final String RED_BACKGROUND = "\u001B[48;2;204;0;0m";
    public static final String GREEN_BACKGROUND = "\u001B[42m";
    public static final String YELLOW_BACKGROUND = "\u001B[43m";
    public static final String BLUE_BACKGROUND = "\u001B[44m";
    public static final String DARK_BLUE_BACKGROUND = "\u001B[48;2;0;51;102m";
    public static final String WHITE_BACKGROUND = "\u001B[47m";
    public static final String ORANGE_BACKGROUND = "\u001B[48;2;255;165;0m";

    /**
     * Displays the welcome message at the start of the game.
     */
    public void displayWelcomeMessage() {
        System.out.println("==================================================");
        System.out.println("             Welcome to Monopoly Game             ");
        System.out.println("==================================================");
    }

    /**
     * Displays the entire game board with all squares.
     * @param board The game board to be displayed.
     */
    public void displayBoard(Board board) {
        int totalSquares = board.getTotalSquares();
        int sideLength = (totalSquares - 4) / 4;
        int squareWidth = 14;
        int squareHeight = 4;

        System.out.println("=".repeat(squareWidth * (sideLength + 2)));

        // Top row
        printRow(board, 0, sideLength + 1, squareWidth, squareHeight, totalSquares, sideLength, false);

        // Middle rows
        for (int i = 0; i < sideLength; i++) {
            int leftIndex = totalSquares - 1 - i;
            int rightIndex = sideLength + 2 + i;
            printMiddleRow(
                    board.getSquare(leftIndex), board.getSquare(rightIndex),
                    squareWidth, squareHeight, leftIndex, rightIndex, totalSquares, sideLength
            );
        }

        // Bottom row
        printRow(board, sideLength * 3 + 3, sideLength * 2 + 2, squareWidth, squareHeight, totalSquares, sideLength, true);
        System.out.println("=".repeat(squareWidth * (sideLength + 2)));
        System.out.println();
    }

    /**
     * Prints a row of squares, either the top or bottom of the board.
     * @param board        The game board.
     * @param start        Starting index of the squares.
     * @param end          Ending index of the squares.
     * @param squareWidth  Width of each square.
     * @param squareHeight Height of each square.
     * @param totalSquares Total number of squares on the board.
     * @param sideLength   Length of one side of the square board.
     * @param isReversed   If true, print squares in reverse order.
     */
    private void printRow(Board board, int start, int end, int squareWidth, int squareHeight, int totalSquares, int sideLength, boolean isReversed) {
        int numSquares = Math.abs(end - start) + 1;
        String[][] coloredCells = new String[numSquares][squareHeight + 2];

        for (int i = 0; i < numSquares; i++) {
            int index = isReversed ? start - i : start + i;
            String color = getColorForSquare(board.getSquare(index), index, totalSquares, sideLength);
            coloredCells[i] = fillCell(color, board.getSquare(index).getName(), squareWidth, squareHeight, board.getSquare(index));
        }

        // Print each line of the row
        for (int line = 0; line < squareHeight + 2; line++) {
            for (String[] coloredCell : coloredCells) {
                System.out.print(coloredCell[line]);
            }
            System.out.println();
        }
    }

    /**
     * Prints the middle rows of the board, containing the left and right columns.
     * @param left         Left square in the row.
     * @param right        Right square in the row.
     * @param squareWidth  Width of each square.
     * @param squareHeight Height of each square.
     * @param leftIndex    Index of the left square.
     * @param rightIndex   Index of the right square.
     * @param totalSquares Total number of squares on the board.
     * @param sideLength   Length of one side of the square board.
     */
    private void printMiddleRow(Square left, Square right, int squareWidth, int squareHeight, int leftIndex, int rightIndex, int totalSquares, int sideLength) {
        String[] leftCell = fillCell(getColorForSquare(left, leftIndex, totalSquares, sideLength), left.getName(), squareWidth, squareHeight, left);
        String[] rightCell = fillCell(getColorForSquare(right, rightIndex, totalSquares, sideLength), right.getName(), squareWidth, squareHeight, right);

        // Print only top border for the first row
        if (leftIndex != totalSquares - 1 && rightIndex != sideLength + 2)
            System.out.printf("%-" + squareWidth + "s%" + (squareWidth * 4) + "s%-" + squareWidth + "s%n",
                    leftCell[0], "", rightCell[0]);

        // Print rows
        for (int line = 1; line <= squareHeight; line++) {
            System.out.printf("%-" + squareWidth + "s%" + (squareWidth * 4) + "s%-" + squareWidth + "s%n",
                    leftCell[line], "", rightCell[line]);
        }
    }

    /**
     * Fills a square's content with borders and formatting.
     * @param color        The background and text color.
     * @param text         The text to display on the square.
     * @param squareWidth  Width of the square.
     * @param squareHeight Height of the square.
     * @param square       The square object for additional details (e.g., price).
     * @return An array of strings representing the square's content.
     */
    String[] fillCell(String color, String text, int squareWidth, int squareHeight, Square square) {
        String[] cell = new String[squareHeight + 2];
        String border = BLACK_TEXT + "+" + "-".repeat(squareWidth - 2) + "+" + RESET;
        String sideBorder = BLACK_TEXT + "|" + RESET;

        // Top border
        cell[0] = border;

        // Prepare content to be displayed
        List<String> contentLines = new ArrayList<>();
        if (text != null && !text.isEmpty()) {
            contentLines.addAll(wrapText(text, squareWidth - 4));
        }
        if (square instanceof PropertySquare property) {
            contentLines.addAll(wrapText("Price: " + property.getPrice(), squareWidth - 4));
            contentLines.addAll(wrapText("Rent: " + property.getRent(), squareWidth - 4));
        }
        while (contentLines.size() < squareHeight) {
            contentLines.add("");
        }
        for (int i = 1; i <= squareHeight; i++) {
            String content = i <= contentLines.size() ? centerAlignText(contentLines.get(i - 1), squareWidth - 4) : " ".repeat(squareWidth - 4);
            cell[i] = String.format("%s%s %-"+ (squareWidth - 4) +"s %s%s", sideBorder, color, content, RESET, sideBorder);
        }

        // Bottom border
        cell[squareHeight + 1] = border;
        return cell;
    }

    /**
     * Centers text within a given width.
     */
    String centerAlignText(String text, int width) {
        if (text.length() > width) {
            // Truncate text if it exceeds the width
            return text.substring(0, width);
        }
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    /**
     * Wraps text to fit within a specified width.
     */
    List<String> wrapText(String text, int width) {
        List<String> wrapped = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (line.length() + word.length() + 1 <= width || line.isEmpty() && word.length() == width) {
                if (!line.isEmpty()) {
                    line.append(" ");
                }
                line.append(word);
            } else {
                wrapped.add(line.toString());
                line = new StringBuilder(word);
            }
        }
        if (!line.isEmpty()) {
            wrapped.add(line.toString());
        }

        return wrapped;
    }

    /**
     * Determines the appropriate color for a square based on its type.
     */
    String getColorForSquare(Square square, int index, int totalSquares, int sideLength) {
        String name = square.getName().toLowerCase();

        if (name.contains("chance")) {
            if (index >= sideLength + 2 && index < sideLength * 2 + 2) return BLACK_BACKGROUND + RED_TEXT; // Right
            else if (index >= sideLength * 3 + 3 && index <= totalSquares - 1) return BLACK_BACKGROUND + YELLOW_TEXT; // Left
            else if (index >= sideLength * 2 + 2 && index < sideLength * 3 + 3) return BLACK_BACKGROUND + DARK_BLUE_TEXT; // Bottom
        }
        else if (name.contains("jail")) return ORANGE_BACKGROUND + BLACK_TEXT;
        else if (name.contains("go") || name.contains("free parking")) return GREEN_BACKGROUND + BLACK_TEXT;
        else if (name.contains("income tax")) return WHITE_BACKGROUND + BLACK_TEXT;

        if(index >= 0 && index < sideLength + 2) return BLUE_BACKGROUND + BLACK_TEXT;
        else if (index >= sideLength + 2 && index < sideLength * 2 + 2) return RED_BACKGROUND + BLACK_TEXT;
        else if (index >= sideLength * 2 + 2 && index < sideLength * 3 + 3) return DARK_BLUE_BACKGROUND + BLACK_TEXT;
        else if (index >= sideLength * 3 + 3 && index <= totalSquares - 1) return YELLOW_BACKGROUND + BLACK_TEXT;

        return RESET;
    }

    /**
     * Displays the current player's turn information.
     */
    public void displayPlayerTurn(int round, Player player) {
        System.out.println("==================================================");
        System.out.printf("                   Round %d                     \n\n", round);
        System.out.println("Current Player Turn: " + player.getName());
        System.out.println("Current Amount of Money: HKD " + player.getMoney());
        System.out.println("Current Position: " + player.getPosition());
        System.out.println("==================================================");
    }

    /**
     * Displays the game's status, including all players' positions and money.
     */
    public void displayGameStatus(List<Player> players) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("                  Game Status                     ");
        System.out.println("==================================================");
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            System.out.printf("Player: %s%nMoney: HKD %d%nPosition: %d%nProperties: %s%n\n",
                    player.getName(), player.getMoney(), player.getPosition(),
                    player.getProperties().isEmpty() ? "None" : player.getProperties());
        }
    }

    /**
     * Displays saved customed boards.
     */
    public void displaySavedBoards() {
        System.out.println("Displaying all saved boards:");
        BoardSaver.displayBoards();
    }

    /**
     * Displays the winner(s) of the game.
     */
    public void displayWinner(List<Player> winners) {
        System.out.println("==================================================");
        if (winners.size() == 1) {
            Player winner = winners.get(0);
            System.out.printf("Congratulations! %s is the winner with HKD %d%n", winner.getName(), winner.getMoney());
        } else {
            System.out.println("It's a tie between:");
            winners.forEach(w -> System.out.printf("%s (HKD %d)%n", w.getName(), w.getMoney()));
        }
        System.out.println("==================================================");
    }
}

