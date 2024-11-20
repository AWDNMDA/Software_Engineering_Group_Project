package Model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final List<Square> squares;

    public Board() {
        squares = new ArrayList<>();
        initializeSquares();
    }

    private void initializeSquares() {
        squares.add(new GoSquare("Go"));
        squares.add(new PropertySquare("Central", 800, 90));
        squares.add(new PropertySquare("Wan Chai", 700, 65));
        squares.add(new IncomeTaxSquare("Income Tax"));
        squares.add(new PropertySquare("Stanley", 600, 60));
        squares.add(new Square("In Jail/Just Visiting") {
            @Override
            public void landOn(Player player) {
                if (player.isInJail()) {
                    System.out.println(player.getName() + " is in Jail.");
                } else {
                    System.out.println(player.getName() + " is just visiting Jail.");
                }
            }
        });

        squares.add(new PropertySquare("Shek O", 400, 10));
        squares.add(new PropertySquare("Mong Kok", 500, 40));
        squares.add(new ChanceSquare("Chance"));
        squares.add(new PropertySquare("Tsing Yi", 400, 15));
        squares.add(new FreeParkingSquare("Free Parking"));

        squares.add(new PropertySquare("Shatin", 700, 75));
        squares.add(new ChanceSquare("Chance"));
        squares.add(new PropertySquare("Tuen Mun", 400, 20));
        squares.add(new PropertySquare("Tai Po", 500, 25));
        squares.add(new GoToJailSquare("Go To Jail", 5));

        squares.add(new PropertySquare("Sai Kung", 400, 10));
        squares.add(new PropertySquare("Yuen Long", 400, 25));
        squares.add(new ChanceSquare("Chance"));
        squares.add(new PropertySquare("Tai O", 600, 25));
    }

    public Square getSquare(int position) { return squares.get(position); }
    public int getTotalSquares() { return squares.size(); }
}
