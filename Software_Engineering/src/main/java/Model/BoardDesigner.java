package Model;

import View.Display;

import java.util.List;
import java.util.Scanner;

/**
 * Class responsible for allowing users to customize the game board.
 * Includes functionality to modify properties and rearrange squares.
 */
public class BoardDesigner {
    private final Scanner scanner;

    /**
     * Constructor with a custom scanner.
     * @param scanner The scanner for user input.
     */
    public BoardDesigner(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Default constructor with a standard scanner.
     */
    public BoardDesigner() {
        this(new Scanner(System.in));
    }

    /**
     * Start the board customization process with a given Scanner.
     *
     * @param board The board to customize.
     */
    public void customizeBoard(Board board) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("                Customization Mode                ");
        System.out.println("==================================================");

        while (true) {
            int choice = getIntInput("Choose an action: \n1.Modify Property \n2.Rearrange Squares \n3.Exit Customization", 1, 3);

            switch (choice) {
                case 1 -> modifyProperty(board);
                case 2 -> rearrangeSquares(board);
                case 3 -> {
                    System.out.println("Exiting customization mode...");
                    return;
                }
            }
        }
    }

    /**
     * Allows users to modify a property square on the board.
     * @param board The board containing the property to modify.
     */
    private void modifyProperty(Board board) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("                   Modify Mode                    ");
        System.out.println("==================================================");

        Display view = new Display();
        view.displayBoard(board);

        int squareIndex = getIntInput("Enter the square number to modify:", 0, board.getTotalSquares() - 1);
        Square square = board.getSquare(squareIndex);

        if (square instanceof PropertySquare property) {
            System.out.println("Current name: " + property.getName());
            String newName = getOptionalInput("Enter new name (leave blank to keep current):");
            if (!newName.isEmpty()) property.setName(newName);

            updateNumericField("Enter new price (leave blank to keep current):", property::setPrice);
            updateNumericField("Enter new rent (leave blank to keep current):", property::setRent);

            System.out.println("Property updated successfully.\n");
            view.displayBoard(board);
        } else {
            System.out.println("The selected square is not a property.\n");
        }
    }

    /**
     * Rearranges two squares on the board.
     * @param board The board containing the squares to rearrange.
     */
    private void rearrangeSquares(Board board) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("                  Rearrange Mode                  ");
        System.out.println("==================================================");

        Display view = new Display();
        view.displayBoard(board);

        int pos1 = getIntInput("Enter the first position to swap:", 0, board.getSquares().size() - 1);
        int pos2 = getIntInput("Enter the second position to swap:", 0, board.getSquares().size() - 1);

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
     * Prompts the user for a numeric input and validates it.
     * @param prompt The message to display to the user.
     * @param min    Minimum valid value.
     * @param max    Maximum valid value.
     * @return The validated input.
     */
    private int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.println(prompt);
                System.out.printf("Enter a number between %d and %d: ", min, max);
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
            } catch (Exception e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Updates a numeric field with user input, if provided.
     * @param prompt    The message to display to the user.
     * @param setter    The method to call to update the field.
     */
    private void updateNumericField(String prompt, java.util.function.IntConsumer setter) {
        String input = getOptionalInput(prompt);
        if (!input.isEmpty()) {
            try {
                setter.accept(Integer.parseInt(input));
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Keeping the current value.");
            }
        }
    }

    /**
     * Prompts the user for optional input.
     * @param prompt The message to display to the user.
     * @return The user's input, or an empty string if no input was given.
     */
    private String getOptionalInput(String prompt) {
        System.out.println(prompt);
        return scanner.nextLine().trim();
    }
}