package Model;

import java.util.List;
import java.util.Scanner;

public class BoardDesigner {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Start the board customization process.
     *
     * @param board The board to customize.
     */
    public void customizeBoard(Board board) {
        System.out.println("Entering board customization mode...");
        while (true) {
            System.out.println("Choose an action: 1. Modify Property 2. Rearrange Squares 3. Exit Customization");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> modifyProperty(board);
                case 2 -> rearrangeSquares(board);
                case 3 -> {
                    System.out.println("Exiting customization mode.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Modify the properties on the board.
     *
     * @param board The board to modify.
     */
    private void modifyProperty(Board board) {
        displayProperties(board);
        System.out.println("Enter the square number to modify:");
        int squareIndex = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (squareIndex >= 0 && squareIndex < board.getTotalSquares()) {
            Square square = board.getSquare(squareIndex);
            if (square instanceof PropertySquare property) {
                System.out.println("Current name: " + property.getName());
                System.out.println("Enter new name (leave blank to keep current):");
                String newName = scanner.nextLine();
                if (!newName.isEmpty()) {
                    property.setName(newName);
                }

                System.out.println("Current price: " + property.getPrice());
                System.out.println("Enter new price (leave blank to keep current):");
                String newPrice = scanner.nextLine();
                if (!newPrice.isEmpty()) {
                    property.setPrice(Integer.parseInt(newPrice));
                }

                System.out.println("Current rent: " + property.getRent());
                System.out.println("Enter new rent (leave blank to keep current):");
                String newRent = scanner.nextLine();
                if (!newRent.isEmpty()) {
                    property.setRent(Integer.parseInt(newRent));
                }

                System.out.println("Property updated successfully.");
            } else {
                System.out.println("The selected square is not a property.");
            }
        } else {
            System.out.println("Invalid square number.");
        }
    }

    /**
     * Rearrange squares on the board.
     *
     * @param board The board to rearrange.
     */
    private void rearrangeSquares(Board board) {
        List<Square> squares = board.getSquares();
        System.out.println("Current board layout:");
        for (int i = 0; i < squares.size(); i++) {
            System.out.println(i + ": " + squares.get(i).getName());
        }

        System.out.println("Enter two positions to swap (0-" + (squares.size() - 1) + "):");
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if (pos1 >= 0 && pos1 < squares.size() && pos2 >= 0 && pos2 < squares.size() && pos1 != pos2) {
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
}
