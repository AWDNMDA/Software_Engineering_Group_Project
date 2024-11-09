import Controller.*;
import Model.*;
import View.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Player> players = new ArrayList<>();

        System.out.println("Enter number of players (2-6): ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numPlayers; i++) {
            System.out.println("Enter name for player " + (i + 1) + ": ");
            String name = scanner.nextLine();
            players.add(new Player(name));
        }

        Board board = new Board();
        Game game = new Game(players, board);
        Display view = new Display();
        GameController controller = new GameController(game, view);

        controller.startGame();
    }
}