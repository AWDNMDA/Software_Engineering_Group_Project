package Model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class used to manage the board in the Monopoly game.
 * The board consists of a list of squares, each representing a location on the game board.
 * It provides methods for retrieving squares and managing the layout.
 * This class implements the Serializable interface, allowing it to be saved and loaded for game persistence.
 */
public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Square> squares;

    /**
     * Constructor for the Board class.
     * Initializes the board with a predefined set of squares.
     */
    public Board() {
        squares = new ArrayList<>();
        initializeSquares();
    }

    /**
     * Initializes the squares on the board with default properties.
     * This includes Go, Property, Chance, Income Tax, Free Parking, and Jail squares.
     */
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

    /**
     * Retrieves a square at a specific position on the board.
     * The position is wrapped around if it exceeds the board size.
     * @param position The index of the square to retrieve.
     * @return The square at the specified position.
     */
    public Square getSquare(int position) {
        int wrappedPosition = (position % squares.size() + squares.size()) % squares.size();
        return squares.get(wrappedPosition);
    }

    /**
     * Retrieves the total number of squares on the board.
     * @return The total number of squares.
     */
    public int getTotalSquares() { return squares.size(); }

    /**
     * Retrieves the list of all squares on the board.
     * @return A list of squares.
     */
    public List<Square> getSquares() { return squares; }
}
