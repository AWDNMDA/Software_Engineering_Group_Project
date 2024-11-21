package View;
import Model.*;

import java.util.List;

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

    public void displayWelcomeMessage() {
        System.out.println("==================================================");
        System.out.println("             Welcome to Monopoly Game             ");
        System.out.println("==================================================");
    }

    public void displayBoard(Board board) {
        int totalSquares = board.getTotalSquares();
        int sideLength = (totalSquares - 4) / 4;
        int squareWidth = 14;

        System.out.println("=".repeat(squareWidth * (sideLength + 2)));

        // Top row (first to sideLength + 1 squares)
        printRow(board, 0, sideLength + 1, squareWidth, totalSquares, sideLength, false);

        // Middle rows (squares on left and right sides)
        for (int i = 0; i < sideLength; i++) {
            int leftIndex = totalSquares - 1 - i;
            int rightIndex = sideLength + 2 + i;
            printMiddleRow(
                    board.getSquare(leftIndex), board.getSquare(rightIndex), squareWidth,
                    leftIndex, rightIndex, totalSquares, sideLength);
        }

        // Bottom row (squares 16 to 11 in reverse order)
        printRow(board, sideLength * 3 + 3, sideLength * 2 + 2, squareWidth, totalSquares, sideLength, true);
        System.out.println("=".repeat(squareWidth * (sideLength + 2)));
        System.out.println();
    }

    private void printRow(Board board, int start, int end, int squareWidth, int totalSquares, int sideLength, boolean isReversed) {
        int numSquares = Math.abs(end - start) + 1;
        String[][] coloredCells = new String[numSquares][5]; // Each cell now has 5 lines

        for (int i = 0; i < numSquares; i++) {
            int index = isReversed ? start - i : start + i;
            String color = getColorForSquare(board.getSquare(index), index, totalSquares, sideLength);
            coloredCells[i] = fillCell(color, board.getSquare(index).getName(), squareWidth);
        }

        // Print each row of the cells
        for (int line = 0; line < 5; line++) { // Now includes borders
            for (String[] coloredCell : coloredCells) {
                System.out.print(coloredCell[line]);
            }
            System.out.println();
        }
    }

    private void printMiddleRow(Square left, Square right, int squareWidth, int leftIndex, int rightIndex, int totalSquares, int sideLength) {
        String[] leftCell = fillCell(getColorForSquare(left, leftIndex, totalSquares, sideLength), left.getName(), squareWidth);
        String[] rightCell = fillCell(getColorForSquare(right, rightIndex, totalSquares, sideLength), right.getName(), squareWidth);

        // Print only top border for the first row
        if (leftIndex != totalSquares - 1 && rightIndex != sideLength + 2)
            System.out.printf("%-" + squareWidth + "s%" + (squareWidth * 4) + "s%-" + squareWidth + "s%n",
                leftCell[0], "", rightCell[0]);

        // Print content rows
        for (int line = 1; line <= 3; line++) {
            System.out.printf("%-" + squareWidth + "s%" + (squareWidth * 4) + "s%-" + squareWidth + "s%n",
                    leftCell[line], "", rightCell[line]);
        }
    }

    private String[] fillCell(String color, String text, int squareWidth) {
        String[] cell = new String[5];
        String border = BLACK_TEXT + "+" + "-".repeat(squareWidth - 2) + "+" + RESET; // Top/bottom border
        String sideBorder = BLACK_TEXT + "|" + RESET; // Side borders for content

        // Top border
        cell[0] = border;

        // Content with side borders and color
        String[] wrappedText = wrapText(text, squareWidth - 4, 3); // Adjusted width for borders
        for (int i = 1; i <= 3; i++) {
            String content = i <= wrappedText.length ? wrappedText[i - 1] : " ".repeat(squareWidth - 4);
            cell[i] = String.format("%s%s %-"+ (squareWidth - 4) +"s %s%s", sideBorder, color, content, RESET, sideBorder);
        }

        // Bottom border
        cell[4] = border;

        return cell;
    }

    private String[] wrapText(String text, int width, int maxLines) {
        String[] wrapped = new String[maxLines];
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        int currentLine = 0;

        for (String word : words) {
            if (line.length() + word.length() <= width) {
                if (!line.isEmpty()) line.append(" ");
                line.append(word);
            } else {
                wrapped[currentLine++] = centerAlignText(line.toString(), width);
                line = new StringBuilder(word);
                if (currentLine >= maxLines) break;
            }
        }
        if (currentLine < maxLines) wrapped[currentLine++] = centerAlignText(line.toString(), width);
        while (currentLine < maxLines) wrapped[currentLine++] = " ".repeat(width);

        return wrapped;
    }

    private String centerAlignText(String text, int width) {
        int padding = Math.max(0, (width - text.length()) / 2);
        return " ".repeat(padding) + text + " ".repeat(width - padding - text.length());
    }

    private String getColorForSquare(Square square, int index, int totalSquares, int sideLength) {
        String name = square.getName().toLowerCase();

        if (name.contains("chance")) {
            if (index >= sideLength + 2 && index < sideLength * 2 + 2) return BLACK_BACKGROUND + RED_TEXT; // Right
            else if (index >= sideLength * 3 + 3 && index <= totalSquares - 1) return BLACK_BACKGROUND + YELLOW_TEXT; // Left
            else if (index >= sideLength * 2 + 2 && index < sideLength * 3 + 3) return BLACK_BACKGROUND + DARK_BLUE_TEXT; // Bottom
        }

        if (name.contains("jail")) return ORANGE_BACKGROUND + BLACK_TEXT;
        else if (name.contains("go") || name.contains("free parking")) return GREEN_BACKGROUND + BLACK_TEXT;
        else if (name.contains("shatin") || name.contains("tuen mun") || name.contains("tai po")) return DARK_BLUE_BACKGROUND + BLACK_TEXT;
        else if (name.contains("shek o") || name.contains("mong kok") || name.contains("tsing yi")) return RED_BACKGROUND + BLACK_TEXT;
        else if (name.contains("wan chai") || name.contains("central") || name.contains("stanley")) return BLUE_BACKGROUND + BLACK_TEXT;
        else if (name.contains("sai kung") || name.contains("yuen long") || name.contains("tai o")) return YELLOW_BACKGROUND + BLACK_TEXT;
        else if (name.contains("income tax")) return WHITE_BACKGROUND + BLACK_TEXT;

        return RESET;
    }

    public void displayPlayerTurn(int round, Player player) {
        System.out.println("==================================================");
        System.out.printf("                   Round %d                     \n\n", round);
        System.out.println("Current Player Turn: " + player.getName());
        System.out.println("Current Amount of Money: HKD " + player.getMoney());
        System.out.println("Current Position: " + player.getPosition());
        System.out.println("==================================================");
    }

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

    public void displaySavedBoards() {
        System.out.println("Displaying all saved boards:");
        BoardSaver.displayBoards();
    }

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

