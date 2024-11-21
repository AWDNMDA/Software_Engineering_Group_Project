package Model;

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
        System.out.println("Entering board customization mode...");
        while (true) {
            System.out.println("Choose an action: 1. Modify Property 2. Rearrange Squares 3. Exit Customization");
            int choice = getIntInput(scanner, 1, 3);

            switch (choice) {
                case 1 -> modifyProperty(board, scanner);
                case 2 -> rearrangeSquares(board, scanner);
                case 3 -> {
                    System.out.println("Exiting customization mode.");
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
        displayProperties(board);
        System.out.println("Enter the square number to modify:");
        int squareIndex = getIntInput(scanner, 0, board.getTotalSquares() - 1);

        Square square = board.getSquare(squareIndex);
        if (square instanceof PropertySquare property) {
            System.out.println("Current name: " + property.getName());
            System.out.println("Enter new name (leave blank to keep current):");
            String newName = scanner.nextLine();
            if (!newName.isEmpty()) property.setName(newName);

            System.out.println("Current price: " + property.getPrice());
            System.out.println("Enter new price (leave blank to keep current):");
            String newPrice = scanner.nextLine();
            if (!newPrice.isEmpty()) property.setPrice(Integer.parseInt(newPrice));

            System.out.println("Current rent: " + property.getRent());
            System.out.println("Enter new rent (leave blank to keep current):");
            String newRent = scanner.nextLine();
            if (!newRent.isEmpty()) property.setRent(Integer.parseInt(newRent));

            System.out.println("Property updated successfully.");
        } else {
            System.out.println("The selected square is not a property.");
        }
    }

    /**
     * Rearrange squares on the board.
     *
     * @param board   The board to rearrange.
     * @param scanner The scanner for user input.
     */
    private void rearrangeSquares(Board board, Scanner scanner) {
        List<Square> squares = board.getSquares();
        System.out.println("Current board layout:");
        for (int i = 0; i < squares.size(); i++) {
            System.out.println(i + ": " + squares.get(i).getName());
        }

        System.out.println("Enter two positions to swap:");
        int pos1 = getIntInput(scanner, 0, squares.size() - 1);
        int pos2 = getIntInput(scanner, 0, squares.size() - 1);

        if (pos1 != pos2) {
            Square temp = squares.get(pos1);
            squares.set(pos1, squares.get(pos2));
            squares.set(pos2, temp);
            System.out.println("Squares swapped successfully.");
        } else {
            System.out.println("Invalid positions. Swap cancelled.");
        }
    }

    /**
     * Display all properties on the board.
     *
     * @param board The board to display.
     */
    private void displayProperties(Board board) {
        System.out.println("Property Squares on the Board:");
        for (int i = 0; i < board.getSquares().size(); i++) {
            Square square = board.getSquare(i);
            if (square instanceof PropertySquare property) {
                System.out.println(i + ": " + square.getName() + " - Price: " + property.getPrice() + ", Rent: " + property.getRent());
            }
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
                int input = scanner.nextInt();
                scanner.nextLine(); // Consume newline
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (Exception e) {
                scanner.nextLine(); // Clear invalid input
                System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
            }
        }
    }
}
