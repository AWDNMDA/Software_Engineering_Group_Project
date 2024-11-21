package Controller;

import Model.*;
import View.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameController {
    private final Game game;
    private final Display view;

    public GameController(Game game, Display view) {
        this.game = game;
        this.view = view;
    }

    /**
     * Initialize and start the game.
     */
    public static void initializeAndStartGame() {
        Scanner scanner = new Scanner(System.in);
        List<Player> players = new ArrayList<>();
        Display view = new Display();

        view.displayWelcomeMessage();
        int numPlayers = getNumberOfPlayers(scanner);

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Player " + i + ", enter your name: ");
            String name = scanner.nextLine().trim();
            players.add(new Player(name));
        }

        Board board = selectOrCustomizeBoard(scanner, view);
        Game game = new Game(players, board);
        GameController controller = new GameController(game, view);

        controller.startGame();
    }

    /**
     * Main game loop.
     */
    public void startGame() {
        view.displayBoard(game.getBoard());
        while (!game.isGameOver()) {
            playRound();
        }
        declareWinner();
    }

    /**
     * Plays a single round for the current player.
     */
    private void playRound() {
        view.displayPlayerTurn(game.getRoundCount(), game.getCurrentPlayer());
        game.playTurn();
        view.displayGameStatus(game.getPlayers());
    }

    /**
     * Declares and displays the winner(s) after the game ends.
     */
    private void declareWinner() {
        List<Player> winners = game.getWinner();
        view.displayWinner(winners);
    }

    /**
     * Gets the number of players for the game.
     *
     * @param scanner Input scanner.
     * @return Valid number of players between 2 and 6.
     */
    private static int getNumberOfPlayers(Scanner scanner) {
        int numPlayers;
        while (true) {
            try {
                System.out.print("Enter the number of players (2-6): ");
                numPlayers = scanner.nextInt();
                scanner.nextLine();
                if (numPlayers >= 2 && numPlayers <= 6) {
                    break;
                } else {
                    System.out.println("Please enter a valid number between 2 and 6.");
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 2 and 6.");
                scanner.nextLine();
            }
        }
        return numPlayers;
    }

    private static Board selectOrCustomizeBoard(Scanner scanner, Display view) {
        while (true) {
            System.out.println("Would you like to: 1. Use Default Board 2. Load Saved Board 3. Customize Board");
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number (1-3).");
                scanner.nextLine(); // Clear invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    return new Board(); // Default board
                case 2:
                    if (BoardSaver.hasSavedBoards()) { // Check if saved boards exist
                        view.displaySavedBoards();
                        return BoardSaver.loadBoard(scanner);
                    } else {
                        System.out.println("No saved boards available. Using default board.");
                        return new Board();
                    }
                case 3:
                    Board customBoard = new Board(); // Start with a default board
                    BoardDesigner boardDesigner = new BoardDesigner();
                    boardDesigner.customizeBoard(customBoard);
                    System.out.println("Would you like to save this board? (yes/no)");
                    if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                        System.out.println("Enter a name for the board:");
                        String name = scanner.nextLine();
                        BoardSaver.saveBoard(customBoard, name);
                    }
                    return customBoard;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
