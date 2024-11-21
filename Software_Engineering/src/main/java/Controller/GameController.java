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
        int numPlayers = getValidatedIntegerInput(scanner, "Enter the number of players (2-6): ", 2, 6);

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

    private static Board selectOrCustomizeBoard(Scanner scanner, Display view) {
        int choice = getValidatedIntegerInput(scanner,
                "Would you like to: 1. Use Default Board 2. Load Saved Board 3. Customize Board: ",
                1, 3);

        switch (choice) {
            case 1 -> {
                return new Board();
            }
            case 2 -> {
                if (BoardSaver.hasSavedBoards()) {
                    view.displaySavedBoards();
                    return BoardSaver.loadBoard(scanner);
                }
                System.out.println("No saved boards available. Using default board.");
                return new Board();
            }
            case 3 -> {
                Board customBoard = new Board();
                BoardDesigner boardDesigner = new BoardDesigner();
                boardDesigner.customizeBoard(customBoard);
                System.out.println("Would you like to save this board? (yes/no)");
                if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                    System.out.println("Enter a name for the board:");
                    String name = scanner.nextLine();
                    BoardSaver.saveBoard(customBoard, name);
                }
                return customBoard;
            }
            default -> throw new IllegalStateException("Unexpected value: " + choice);
        }
    }

    private static int getValidatedIntegerInput(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int input = scanner.nextInt();
                scanner.nextLine();
                if (input >= min && input <= max) {
                    return input;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
                scanner.nextLine();
            }
        }
    }
}
