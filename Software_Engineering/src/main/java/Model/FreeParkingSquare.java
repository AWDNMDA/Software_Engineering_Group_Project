package Model;

/**
 * Represents a "Free Parking" square in the game.
 * This square serves as a neutral space where no specific actions occur.
 * Players landing on this square are neither rewarded nor penalized.
 */
public class FreeParkingSquare extends Square {

    /**
     * Constructor for the FreeParkingSquare class.
     * @param name The name of the square (e.g., "Free Parking").
     */
    public FreeParkingSquare(String name) {
        super(name);
    }

    /**
     * Method called when a player lands on this square.
     * No actions are taken; this is a neutral square.
     * @param player The player who lands on the square.
     */
    @Override
    public void landOn(Player player) {
        System.out.println(player.getName() + " landed on Free Parking. Nothing happens.");
    }
}
