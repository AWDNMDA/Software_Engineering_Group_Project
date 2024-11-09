package Model;

import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {
    private List<Player> players;
    final private Board board;
    private int currentPlayerIndex;
    private int roundCount;
    final private Random random;
    final private Scanner scanner;

    public Game(List<Player> players, Board board) {
        this.players = players;
        this.board = board;
        this.currentPlayerIndex = 0;
        this.roundCount = 0;
        this.random = new Random();
        this.scanner = new Scanner(System.in);
    }

    public List<Player> getPlayers(){return players;}
    public Board getBoard(){return board;}

    public void playTurn() {
        Player currentPlayer = players.get(currentPlayerIndex);
        if (currentPlayer.isInJail()) {
            handleJail(currentPlayer);
        } else {
            System.out.println(currentPlayer.getName() + ", it's your turn. Press Enter to roll the dice.");
            scanner.nextLine();
            int diceRoll = rollDice();
            System.out.println("You rolled a " + diceRoll);
            currentPlayer.move(diceRoll);
            Square square = board.getSquare(currentPlayer.getPosition());
            square.landOn(currentPlayer);
        }
        checkPlayerStatus(currentPlayer);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        roundCount++;
    }
    private int rollDice() {
        return random.nextInt(4) + 1 + random.nextInt(4) + 1;
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
        player.setMoney(player.getMoney() - 150);
        player.setInJail(false);
        player.setJailTurns(0);
        System.out.println("You paid the fine. Press Enter to roll the dice.");
        scanner.nextLine();
        int diceRoll = rollDice();
        System.out.println("You rolled a " + diceRoll);
        player.move(diceRoll);
    }
    private void rollForDoubles(Player player) {
        System.out.println("Press Enter to roll for doubles.");
        scanner.nextLine();
        int die1 = random.nextInt(4) + 1;
        int die2 = random.nextInt(4) + 1;
        System.out.println("You rolled a " + die1 + " and a " + die2);
        if (die1 == die2) {
            System.out.println("You rolled doubles and are out of jail!");
            player.setInJail(false);
            player.setJailTurns(0);
            player.move(die1 + die2);
        } else {
            player.setJailTurns(player.getJailTurns() + 1);
            if (player.getJailTurns() == 2) {
                System.out.println("You must pay HKD 150 to get out of jail.");
                payFineAndMove(player);
            }
        }
    }


    private void checkPlayerStatus(Player player) {
        if (player.getMoney() < 0) {
            System.out.println(player.getName() + " is bankrupt and out of the game.");
            players.remove(player);
        }
    }
    public List<Player> getWinner() {
        int maxMoney = players.stream().mapToInt(Player::getMoney).max().orElse(0);
        return players.stream().filter(p -> p.getMoney() == maxMoney).collect(Collectors.toList());
    }

    public boolean isGameOver() {
        return players.size() <= 1 || roundCount >= 100;
    }



}


