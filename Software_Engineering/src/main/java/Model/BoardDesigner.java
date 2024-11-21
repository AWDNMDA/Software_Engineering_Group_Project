package Model;

import View.Display;

import java.util.List;
import java.util.Scanner;

public class BoardDesigner {
    private final Scanner scanner;

    public BoardDesigner(Scanner scanner) {
        this.scanner = scanner;
    }

    public BoardDesigner() {
        this(new Scanner(System.in));
    }

    /**
     * Start the board customization process.
     *
     * @param board The board to customize.
     */
    public void customizeBoard(Board board) {
        customizeBoard(board, this.scanner);
    }

    /**
     * Start the board customization process with a given Scanner.
     *
     * @param board   The board to customize.
     * @param scanner The scanner for user input.
     */
    public void customizeBoard(Board board, Scanner scanner) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("                Customization Mode                ");
        System.out.println("==================================================");

        while (true) {
            System.out.println("Choose an action: \n1.Modify Property \n2.Rearrange Squares \n3.Exit Customization");
            int choice = getIntInput(scanner, 1, 3);

            switch (choice) {
                case 1 -> modifyProperty(board, scanner);
                case 2 -> rearrangeSquares(board, scanner);
                case 3 -> {
                    System.out.println("Exiting customization mode...");
                    return;
                }
            }
        }
    }

    /**
     * Modify the properties on the board.
     *
     * @param board   The board to modify.
     * @param scanner The scanner for user input.
     */
    private void modifyProperty(Board board, Scanner scanner) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("                   Modify Mode                    ");
        System.out.println("==================================================");

        Display view = new Display();
        view.displayBoard(board);

        System.out.println("\nEnter the square number to modify:");
        int squareIndex = getIntInput(scanner, 0, board.getTotalSquares() - 1);
        Square square = board.getSquare(squareIndex);

        if (square instanceof PropertySquare property) {
            System.out.println("Current name: " + property.getName());
            String newName = getOptionalInput(scanner, "Enter new name (leave blank to keep current):");
            if (!newName.isEmpty()) property.setName(newName);

            String newPrice = getOptionalInput(scanner, "Enter new price (leave blank to keep current):");
            if (!newPrice.isEmpty()) {
                try {
                    property.setPrice(Integer.parseInt(newPrice));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price input. Keeping the current price.");
                }
            }

            String newRent = getOptionalInput(scanner, "Enter new rent (leave blank to keep current):");
            if (!newRent.isEmpty()) {
                try {
                    property.setRent(Integer.parseInt(newRent));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid rent input. Keeping the current rent.");
                }
            }

            System.out.println("Property updated successfully.\n");
            view.displayBoard(board);
        } else {
            System.out.println("The selected square is not a property.\n");
        }
    }

    /**
     * Rearrange squares on the board.
     *
     * @param board   The board to rearrange.
     * @param scanner The scanner for user input.
     */
    private void rearrangeSquares(Board board, Scanner scanner) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("                  Rearrange Mode                  ");
        System.out.println("==================================================");

        Display view = new Display();
        view.displayBoard(board);

        System.out.println("Enter two positions to swap:");
        int pos1 = getIntInput(scanner, 0, board.getSquares().size() - 1);
        int pos2 = getIntInput(scanner, 0, board.getSquares().size() - 1);

        if (pos1 != pos2) {
            List<Square> squares = board.getSquares();
            Square temp = squares.get(pos1);

            squares.set(pos1, squares.get(pos2));
            squares.set(pos2, temp);

            System.out.println("Squares swapped successfully.\n");
            view.displayBoard(board);
        } else {
            System.out.println("Invalid positions. Swap cancelled.");
        }
    }

    /**
     * Get an integer input within a specified range.
     *
     * @param scanner The scanner for user input.
     * @param min     Minimum valid value.
     * @param max     Maximum valid value.
     * @return Validated integer input.
     */
    private int getIntInput(Scanner scanner, int min, int max) {
        while (true) {
            try {
                System.out.printf("Enter a number between %d and %d: ", min, max);
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("Invalid input. Please try again!");
                }
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please try again!");
            }
        }
    }

    private String getOptionalInput(Scanner scanner, String prompt) {
        System.out.println(prompt);
        String input = scanner.nextLine().trim();
        return input.isEmpty() ? "" : input;
    }
}