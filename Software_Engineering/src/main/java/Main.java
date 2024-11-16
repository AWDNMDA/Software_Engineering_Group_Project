import Controller.*;
import Model.*;
import View.*;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Player> players = new ArrayList<>();
        Display view = new Display();

        view.displayWelcomeMessage();
        int numPlayers = getNumberOfPlayers(scanner);

        for (int i = 1; i <= numPlayers; i++) {
            System.out.print("Player " + (i) + ", enter your name" + ": ");
            String name = scanner.nextLine().trim();
            players.add(new Player(name));
        }

        Board board = new Board();
        Game game = new Game(players, board);
        GameController controller = new GameController(game, view);

        controller.startGame();
    }

    private static int getNumberOfPlayers(Scanner scanner) {
        int numPlayers;
        while (true) {
            try {
                System.out.print("Enter the number of players (2-6): ");
                numPlayers = scanner.nextInt();
                scanner.nextLine(); // Clear the newline
                if (numPlayers >= 2 && numPlayers <= 6) {
                    break;
                } else {
                    System.out.println("Please enter a valid number between 2 and 6.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number between 2 and 6.");
                scanner.nextLine(); // Clear the invalid input
            }
        }
        return numPlayers;
    }
}