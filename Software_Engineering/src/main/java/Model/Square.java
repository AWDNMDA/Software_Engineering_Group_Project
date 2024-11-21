package Model;

import java.io.Serial;
import java.io.Serializable;

/**
 * Abstract base class for all types of squares on the game board.
 * Each square has a name and an action associated with it when a player lands on it.
 */
public abstract class Square implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    /**
     * Constructor for the Square class.
     * @param name The name of the square.
     */
    public Square(String name) {
        this.name = name;
    }

    /**
     * Abstract method to define the action performed when a player lands on this square.
     * @param player The player who lands on the square.
     */
    public abstract void landOn(Player player);

    /**
     * Retrieves the name of the square.
     * @return The name of the square.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets a new name for the square.
     * @param name The new name of the square.
     */
    public void setName(String name) {
        this.name = name;
    }
}
