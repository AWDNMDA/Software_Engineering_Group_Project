package View;
import Model.*;

import java.util.List;

public class Display {
    public void displayWelcomeMessage() {
        System.out.println("==================================================");
        System.out.println("             Welcome to Monopoly Game             ");
        System.out.println("==================================================");
    }

    public void displayBoard(Board board) {
        System.out.println("Monopoly Board:");
        for (int i = 0; i < board.getTotalSquares(); i++) {
            Square square = board.getSquare(i);
            System.out.println(i + ": " + square.getName());
        }
        System.out.println();
    }

    public void displayPlayerTurn(int round, Player player) {
        System.out.println("==================================================");
        System.out.println("                   Round " + round + "                     ");
        System.out.println();
        System.out.println("Current Player Turn: " + player.getName());
        System.out.println("Current Money: HKD " + player.getMoney());
        System.out.println("Current Position: " + player.getPosition());
        System.out.println("==================================================");
    }

    public void displayPlayerStatus(Player player) {
        System.out.println("Player: " + player.getName());
        System.out.println("Money: HKD " + player.getMoney());
        System.out.println("Position: " + player.getPosition());
        System.out.println("Properties: ");
        if (player.getProperties().isEmpty()) {
            System.out.println("None");
        } else {
            for (PropertySquare property : player.getProperties()) {
                System.out.println(property.getName() + " ");
            }
        }
        System.out.println(player.isInJail() ? "Status: In Jail" : "Status: Not in Jail");
        System.out.println();
    }

    public void displayGameStatus(List<Player> players) {
        System.out.println();
        System.out.println("==================================================");
        System.out.println("                  Game Status                     ");
        System.out.println("==================================================");
        for (Player player : players) {
            displayPlayerStatus(player);
        }
    }

    public void displayWinner(List<Player> winners) {
        System.out.println("==================================================");
        if (winners.size() == 1) {
            Player winner = winners.get(0);
            System.out.println("Congratulations! The winner is " + winner.getName());
            System.out.println("Final Money: HKD " + winner.getMoney());
        } else {
            System.out.println("It's a tie between the following players:");
            for (Player winner : winners) {
                System.out.println(winner.getName() + " with HKD " + winner.getMoney());
            }
        }
        System.out.println("==================================================");
    }
}

