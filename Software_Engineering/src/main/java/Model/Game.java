package Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {
    private final List<Player> players;
    private final Board board;
    private int currentPlayerIndex;
    private int roundCount;
    private final Random random;
    private final Scanner scanner;

    public Game(List<Player> players, Board board) {
        this.players = new ArrayList<>(players);
        this.board = board;
        this.currentPlayerIndex = 0;
        this.roundCount = 0;
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }

    public List<Player> getPlayers(){return players;}
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
    public Board getBoard(){return board;}

    public void playTurn() {
        Player currentPlayer = getCurrentPlayer();
        if (currentPlayer.getMoney() < 0) {
            players.remove(currentPlayer); // Remove bankrupt players
        } else {
            // Simulate the player's turn (e.g., rolling dice, moving, landing on a square)
            System.out.println(currentPlayer.getName() + " is playing their turn.");
        }

        // Move to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private void takeTurn(Player player) {
        System.out.println(player.getName() + ", it's your turn. Press Enter to roll the dice.");
        scanner.nextLine();
        int diceRoll = rollDice();
        System.out.println("You rolled a " + diceRoll + ".");
        movePlayer(player, diceRoll);
    }

    private void movePlayer(Player player, int diceRoll) {
        player.move(diceRoll);
        Square square = board.getSquare(player.getPosition());
        System.out.println("You landed on: " + square.getName());

        if (square instanceof PropertySquare propertySquare) {
            handleProperty(player, propertySquare);  // Handles the property interaction
        } else {
            square.landOn(player);
        }
    }

    private void handleProperty(Player player, PropertySquare propertySquare) {
        if (!propertySquare.isOwned()) {
            System.out.println(propertySquare.getName() + " is available for purchase at HKD " + propertySquare.getPrice() + ".");
            System.out.println("Would you like to buy it? (yes/no)");

            while (true) {
                String choice = scanner.nextLine().trim().toLowerCase();
                if ("yes".equals(choice)) {
                    if (player.getMoney() >= propertySquare.getPrice()) {
                        propertySquare.buyProperty(player);
                    } else {
                        System.out.println("You do not have enough money to buy this property!");
                    }
                    break;
                } else if ("no".equals(choice)) {
                    System.out.println("You chose not to buy the property.");
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            }
        } else if (propertySquare.getOwner() != player) {
            propertySquare.landOn(player);  // Pay rent if owned by another player
        } else {
            System.out.println("You own this property.");
        }
    }


    private int rollDice() {
        return random.nextInt(5) + 1 + random.nextInt(5) + 1;
    }

    private void handleJail(Player player) {
        System.out.println(player.getName() + " is in jail.");

        if (player.getJailTurns() < 2) {
            System.out.println("Press 'T' to pay HKD 150 and get out, or 'F' to roll for doubles.");
            String choice = scanner.nextLine().trim().toUpperCase();

            if (choice.equals("T")) {
                payFineAndMove(player);
            } else {
                rollForDoubles(player);
            }
        } else {
            System.out.println("You must pay HKD 150 to get out of jail.");
            payFineAndMove(player);
        }
    }

    private void payFineAndMove(Player player) {
        if (player.getMoney() >= 150) {
            player.deductMoney(150);
            player.setInJail(false);
            System.out.println(player.getName() + " paid HKD 150 and is now out of jail.");
            takeTurn(player);
        } else {
            System.out.println(player.getName() + " does not have enough money to pay the fine!");
        }
    }

    private void rollForDoubles(Player player) {
        System.out.println(player.getName() + " is rolling for doubles...");
        int die1 = random.nextInt(5) + 1;
        int die2 = random.nextInt(5) + 1;

        System.out.println("You rolled a " + die1 + " and a " + die2);
        if (die1 == die2) {
            System.out.println("You rolled doubles and are out of jail!");
            player.setInJail(false);
            player.setJailTurns(0);
            movePlayer(player, die1 + die2);
        } else {
            System.out.println("No doubles. Jail turns incremented.");
            player.incrementJailTurns();
            if (player.getJailTurns() >= 2) {
                System.out.println("You must pay HKD 150 to get out of jail.");
                payFineAndMove(player);
            }
        }
    }

    private void checkBankruptcy(Player player) {
        if (player.getMoney() < 0) {
            System.out.println(player.getName() + " is bankrupt and has been removed from the game.");
            players.remove(player);
            if (currentPlayerIndex >= players.size()) {
                currentPlayerIndex = 0; // Reset if index is out of bounds
            }
        }
    }

    private void nextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        roundCount++;
    }

    private void checkPlayerStatus(Player player) {
        if (player.getMoney() < 0) {
            System.out.println(player.getName() + " is bankrupt and out of the game.");
            players.remove(player);
        }
    }
    public List<Player> getWinner() {
        int maxMoney = players.stream().mapToInt(Player::getMoney).max().orElse(0);
        return players.stream().filter(p -> p.getMoney() == maxMoney).toList();
    }

    public boolean isGameOver() {
        return players.size() <= 1 || roundCount >= 100;
    }

    public int getRoundCount() { return roundCount; }
}


