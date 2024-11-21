package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The `Game` class manages the flow of the Monopoly game.
 * It handles player actions, turns, dice rolls, property interactions, and game rules.
 */
public class Game {
    private static final int MAX_ROUNDS = 100;
    private static final int JAIL_FINE = 150;

    private final List<Player> players;
    private final Board board;
    private int currentPlayerIndex;
    private int roundCount;
    private final Random random;
    private final Scanner scanner;

    /**
     * Primary constructor for initializing the game with custom components.
     * @param players List of players in the game.
     * @param board   The game board.
     * @param random  Random number generator for dice rolls.
     * @param scanner Scanner for user input.
     */
    public Game(List<Player> players, Board board, Random random, Scanner scanner) {
        this.players = new ArrayList<>(players);
        this.board = board;
        this.random = random;
        this.scanner = scanner;
        this.currentPlayerIndex = 0;
        this.roundCount = 0;
    }

    /**
     * Convenience constructor for initializing the game with default Random and Scanner.
     * @param players List of players in the game.
     * @param board   The game board.
     */
    public Game(List<Player> players, Board board) {
        this(players, board, new Random(), new Scanner(System.in));
    }

    /**
     * @return The list of players in the game.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return The current player whose turn it is.
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * @return The game board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * @return The current round count.
     */
    public int getRoundCount() {
        return roundCount;
    }

    /**
     * Checks if the game is over.
     * @return True if the game is over (one player left or maximum rounds reached), otherwise false.
     */
    public boolean isGameOver() {
        return players.size() <= 1 || roundCount >= MAX_ROUNDS;
    }

    /**
     * Determines the winner(s) of the game based on the highest amount of money.
     * @return A list of players with the highest money.
     */
    public List<Player> getWinner() {
        int maxMoney = players.stream().mapToInt(Player::getMoney).max().orElse(0);
        return players.stream()
                .filter(player -> player.getMoney() == maxMoney)
                .collect(Collectors.toList());
    }

    /**
     * Handles the current player's turn, including handling jail status or taking a regular turn.
     */
    public void playTurn() {
        if (isGameOver()) {
            System.out.println("Game is over!");
            return;
        }
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.isInJail()) {
            handleJail(currentPlayer);
        } else {
            takeTurn(currentPlayer);
        }
        checkBankruptcy();
        nextPlayer();
    }

    /**
     * Handles the actions for a player's turn.
     * Prompts the player to roll the dice and moves them on the board.
     * @param player The player whose turn it is.
     */
    private void takeTurn(Player player) {
        System.out.println(player.getName() + ", it's your turn. Press Enter to roll the dice.");
        scanner.nextLine();
        int diceRoll = rollDice();
        System.out.println(player.getName() + " rolled a " + diceRoll);
        movePlayer(player, diceRoll);
    }


    /**
     * Handles players in jail, including paying fines or rolling for doubles.
     * @param player The player in jail.
     */
    private void handleJail(Player player) {
        System.out.println(player.getName() + " is in jail.");

        if (player.getJailTurns() < 2) {
            String choice = getValidatedInput("Press 'T' to pay HKD " + JAIL_FINE +
                    " and get out, or 'F' to roll for doubles.", "T", "F");
            if ("T".equals(choice)) {
                payFineAndMove(player);
            } else {
                rollForDoubles(player);
            }
        } else {
            System.out.println("You must pay HKD " + JAIL_FINE + " to get out of jail.");
            payFineAndMove(player);
        }
    }

    /**
     * Handles payment of the jail fine and moves the player out of jail.
     * @param player The player paying the fine.
     */
    private void payFineAndMove(Player player) {
        if (player.getMoney() >= JAIL_FINE) {
            player.deductMoney(JAIL_FINE);
            player.setInJail(false);
            System.out.println(player.getName() + " paid HKD " + JAIL_FINE + " and is now out of jail.");
        } else {
            System.out.println(player.getName() + " does not have enough money to pay the fine!");
            checkBankruptcy();
        }
    }

    /**
     * Checks and handles player bankruptcies.
     * Removes players with negative money and resets ownership of their properties.
     */
    void checkBankruptcy() {
        List<Player> bankruptPlayers = players.stream()
                .filter(player -> player.getMoney() < 0)
                .toList();

        for (Player bankruptPlayer : bankruptPlayers) {
            System.out.println("Processing bankruptcy for: " + bankruptPlayer.getName());
            for (Square square : board.getSquares()) {
                if (square instanceof PropertySquare property && bankruptPlayer.equals(property.getOwner())) {
                    property.setOwner(null);
                    System.out.println("Reset ownership for: " + property.getName());
                }
            }
            System.out.println("Removing player: " + bankruptPlayer.getName());
            players.remove(bankruptPlayer);
        }
    }

    /**
     * Moves the player to a new position and handles the square's effect.
     * @param player    The player to move.
     * @param diceRoll  The result of the dice roll.
     */
    private void movePlayer(Player player, int diceRoll) {
        player.move(diceRoll);
        Square square = board.getSquare(player.getPosition());
        System.out.println(player.getName() + " landed on: " + square.getName());

        if (square instanceof GoToJailSquare goToJailSquare) {
            goToJailSquare.landOn(player);
        } else if (square instanceof PropertySquare propertySquare) {
            handleProperty(player, propertySquare);
        } else {
            square.landOn(player);
        }
    }

    /**
     * Moves to the next player's turn.
     */
    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        roundCount++;
    }

    /**
     * Handles interaction with a property square.
     * @param player         The player interacting with the property.
     * @param propertySquare The property square.
     */
    private void handleProperty(Player player, PropertySquare propertySquare) {
        if (!propertySquare.isOwned()) {
            System.out.println(propertySquare.getName() + " is available for purchase at HKD " + propertySquare.getPrice() + ".");
            System.out.println("Would you like to buy it? (yes/no)");

            String choice = getValidatedInput("yes", "no");
            if ("yes".equals(choice)) {
                if (player.getMoney() >= propertySquare.getPrice()) {
                    propertySquare.buyProperty(player);
                } else {
                    System.out.println("You do not have enough money to buy this property!");
                }
            }
        } else if (!propertySquare.getOwner().equals(player)) {
            propertySquare.landOn(player);
        } else {
            System.out.println("You own this property.");
        }
    }

    /**
     * Validates user input against a list of valid options.
     * @param validOptions Valid input options.
     * @return The validated input.
     */
    private String getValidatedInput(String... validOptions) {
        while (true) {
            String input = scanner.nextLine().trim();
            for (String option : validOptions) {
                if (option.equalsIgnoreCase(input)) {
                    return input;
                }
            }
            System.out.println("Invalid input. Please try again.");
        }
    }


    /**
     * Rolls two dice and returns the sum.
     * @return The sum of two dice rolls.
     */
    int rollDice() {
        return random.nextInt(6) + 1 + random.nextInt(6) + 1;
    }

    /**
     * Handles the player's attempt to roll doubles to get out of jail.
     * If the player rolls doubles (both dice show the same value), they are released from jail,
     * their jail turn counter is reset, and they move forward based on the dice roll.
     * If they fail to roll doubles, their jail turn counter is incremented.
     * @param player The player attempting to roll doubles to get out of jail.
     */
    private void rollForDoubles(Player player) {
        System.out.println(player.getName() + " is rolling for doubles...");
        int die1 = random.nextInt(6) + 1;
        int die2 = random.nextInt(6) + 1;

        System.out.println("You rolled a " + die1 + " and a " + die2);
        if (die1 == die2) {
            System.out.println("You rolled doubles and are out of jail!");
            player.setInJail(false);
            player.setJailTurns(0);
            movePlayer(player, die1 + die2);
        } else {
            System.out.println("No doubles. Jail turns incremented.");
            player.incrementJailTurns();
        }
    }
}
