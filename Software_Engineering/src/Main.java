import Controller.*;
import Model.*;
import View.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BoardSaver.saveBoard(new Board(), "Default"); // Default board saved at startup

        while (true) {
            System.out.println("Choose your role: 1. Player 2. Designer 3. Exit");
            int roleChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (roleChoice == 1) {
                startPlayerMode(scanner);
            } else if (roleChoice == 2) {
                startDesignerMode(scanner);
            } else if (roleChoice == 3) {
                System.out.println("Exiting the game.");
                break;
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void startDesignerMode(Scanner scanner) {
        Board board = new Board();
        BoardDesigner designer = new BoardDesigner();
        while (true) {
            System.out.println("Choose an action: 1. Load and Customize 2. Save the Board 3. Back to Main Menu");
            int actionChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (actionChoice) {
                case 1:
                    board = BoardSaver.loadBoard(scanner);
                    designer.customizeBoard(board);
                    break;
                case 2:
                    System.out.println("Enter a name for the board:");
                    String saveName = scanner.nextLine();
                    BoardSaver.saveBoard(board, saveName);
                    System.out.println("Board saved as " + saveName);
                    break;
                case 3:
                    System.out.println("Returning to main menu.");
                    return; // Go back to main menu
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void startPlayerMode(Scanner scanner) {
        System.out.println("Select a board to play on:");
        Board board = BoardSaver.loadBoard(scanner); // Allow player to choose an existing board

        List<Player> players = new ArrayList<>();
        System.out.println("Enter number of players (2-6): ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Enter name for player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }

        Game game = new Game(players, board);
        Display view = new Display();
        GameController controller = new GameController(game, view);

        controller.startGame();
    }
}