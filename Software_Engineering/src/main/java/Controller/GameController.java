package Controller;

import Model.*;
import View.Display;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class used to manage the Monopoly game flow.
 * It interacts with the user through the Display class and handles the game's main logic, including initializing the game,
 * managing player turns, and determining the winner. This class integrates with the Model layer to update the game state
 * and display the current status to the players.
 */
public class GameController {
    private final Game game;
    private final Display view;

    /**
     * Constructor for the GameController class.
     * @param game The game instance containing the players and board.
     * @param view The display instance for interacting with the user.
     */
    public GameController(Game game, Display view) {
        this.game = game;
        this.view = view;
    }

    /**
     * Initializes and starts the game using default input and display instances.
     */
    public static void initializeAndStartGame() {
        initializeAndStartGame(new Scanner(System.in), new Display(), null);
    }

    /**
     * Initializes and starts the game with specified input, display, and game instances.
     * @param scanner The Scanner instance for user input.
     * @param view The Display instance for displaying messages.
     * @param game The Game instance (can be null if creating a new game).
     */
    public static void initializeAndStartGame(Scanner scanner, Display view, Game game) {
        if (game == null) {
            view.displayWelcomeMessage();
            int numPlayers = getValidatedIntegerInput(scanner, "Enter the number of players (2-6): ", 2, 6);
            List<Player> players = createPlayers(scanner, numPlayers);
            Board board = selectOrCustomizeBoard(scanner, view);
            game = new Game(players, board);
        }
        new GameController(game, view).startGame();
    }

    /**
     * Starts the main game loop, displaying the board and handling rounds until the game ends.
     */
    public void startGame() {
        view.displayBoard(game.getBoard());
        while (!game.isGameOver()) {
            playRound();
        }
        declareWinner();
    }

    /**
     * Plays a single round of the game for the current player.
     */
    void playRound() {
        Player currentPlayer = game.getCurrentPlayer();
        System.out.println("Playing round for: " + currentPlayer.getName());
        view.displayPlayerTurn(game.getRoundCount(), currentPlayer);
        game.playTurn();
        view.displayGameStatus(game.getPlayers());
    }

    /**
     * Declares and displays the winner(s) of the game.
     */
    private void declareWinner() {
        List<Player> winners = game.getWinner();
        System.out.println("Game over! Declaring winner(s)...");
        view.displayWinner(winners);
    }

    /**
     * Prompts the user to create players by entering their names.
     * @param scanner The Scanner instance for user input.
     * @param numPlayers The number of players to create.
     * @return A list of created Player instances.
     */
    static List<Player> createPlayers(Scanner scanner, int numPlayers) {
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Player " + i + ", enter your name: ");
            String name = scanner.nextLine().trim();
            players.add(new Player(name));
        }
        return players;
    }

    /**
     * Prompts the user to select or customize a board for the game.
     * @param scanner The Scanner instance for user input.
     * @param view The Display instance for displaying messages.
     * @return The selected or customized Board instance.
     */
    static Board selectOrCustomizeBoard(Scanner scanner, Display view) {
        while (true) {
            try {
                int choice = getValidatedIntegerInput(scanner,
                        "\nWould you like to: \n1. Use Default Board \n2. Load Saved Board \n3. Customize Board\nEnter a number between 1 and 3: ",
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
                        return customizeBoard(scanner, new BoardDesigner());
                    }
                    default -> throw new IllegalStateException("Unexpected choice: " + choice);
                }
            } catch (Exception e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Customizes a new board and optionally saves it.
     * @param scanner The Scanner instance for user input.
     * @param boardDesigner The BoardDesigner instance for customizing the board.
     * @return The customized Board instance.
     */
    static Board customizeBoard(Scanner scanner, BoardDesigner boardDesigner) {
        Board customBoard = new Board();
        boardDesigner.customizeBoard(customBoard);

        System.out.println("Would you like to save this board? (yes/no)");
        if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
            System.out.print("Enter a name for the board: ");
            String name = scanner.nextLine().trim();
            BoardSaver.saveBoard(customBoard, name);
        }
        return customBoard;
    }

    /**
     * Prompts the user for a validated integer input within a specified range.
     * @param scanner The Scanner instance for user input.
     * @param prompt The prompt message to display to the user.
     * @param min The minimum allowed value.
     * @param max The maximum allowed value.
     * @return The validated integer input from the user.
     */
    static int getValidatedIntegerInput(Scanner scanner, String prompt, int min, int max) {
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
