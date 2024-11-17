package View;
import Model.*;

import java.util.List;

public class Display {
    public void displayBoard(Board board) {
        System.out.println("Monopoly Board:");
        for (int i = 0; i < 20; i++) {
            Square square = board.getSquare(i);
            System.out.println(i + ": " + square.getName());
        }
    }
    public void displayPlayerStatus(Player player) {
        System.out.println("Player: " + player.getName());
        System.out.println("Money: HKD " + player.getMoney());
        System.out.println("Position: " + player.getPosition());
        System.out.println("Properties: ");
        for (PropertySquare property : player.getProperties()) {
            System.out.println(" - " + property.getName());
        }
        System.out.println(player.isInJail() ? "In Jail" : "Not in Jail");
        System.out.println();
    }

    public void displayGameStatus(List<Player> players) {
        System.out.println("Game Status:");
        for (Player player : players) {
            displayPlayerStatus(player);
        }
    }
    public void displayWinner(List<Player> winners) {
        if (winners.size() == 1) {
            Player winner = winners.get(0);
            System.out.println("The winner is " + winner.getName() + " with HKD " + winner.getMoney());
        } else {
            System.out.println("It's a tie between:");
            for (Player winner : winners) {
                System.out.println(winner.getName() + " with HKD " + winner.getMoney());
            }
        }
    }

}

