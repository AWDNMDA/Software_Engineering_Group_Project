package Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game.
 * Each player has a name, money, position on the board, jail status, properties owned, and jail-related attributes.
 */
public class Player {
    private final String name;
    private int money;
    private int position;
    private boolean inJail;
    private int jailTurns;
    private final List<PropertySquare> properties;

    /**
     * Constructor for the Player class.
     * Initializes the player with default values.
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.money = 1500;
        this.position = 0;
        this.inJail = false;
        this.jailTurns = 0;
        this.properties = new ArrayList<>();
    }

    /**
     * Retrieves the player's name.
     * @return The name of the player.
     */
    public String getName() { return name; }

    /**
     * Retrieves the player's current money.
     * @return The player's money.
     */
    public int getMoney() { return money; }

    /**
     * Retrieves the player's current position on the board.
     * @return The player's position.
     */
    public int getPosition() { return position; }

    /**
     * Checks if the player is in jail.
     * @return True if the player is in jail, otherwise false.
     */
    public boolean isInJail() { return inJail; }

    /**
     * Returns the names of the properties owned by the player.
     *
     * @return A list of property names.
     */
    public List<String> getProperties() {
        return properties.stream()
                .map(PropertySquare::getName)
                .toList();
    }

    /**
     * Retrieves the number of turns the player has spent in jail.
     * @return The number of jail turns.
     */
    public int getJailTurns() { return jailTurns; }

    /**
     * Sets the player's money.
     * @param money The new amount of money for the player.
     */
    public void setMoney(int money) { this.money = money; }

    /**
     * Sets the player's position on the board.
     * @param position The new position for the player.
     */
    public void setPosition(int position) { this.position = position; }

    /**
     * Sets the player's jail status.
     * @param inJail True if the player is in jail, otherwise false.
     */
    public void setInJail(boolean inJail) { this.inJail = inJail; }

    /**
     * Sets the number of turns the player has spent in jail.
     * @param turns The number of jail turns.
     */
    public void setJailTurns(int turns) { this.jailTurns = turns; }

    /**
     * Moves the player a specified number of steps forward.
     * The player's position wraps around if it exceeds the total number of squares.
     * @param steps The number of steps to move.
     */
    public void move(int steps) { position = (position + steps) % 20; }

    /**
     * Adds money to the player's balance.
     * @param amount The amount of money to add. Must be positive.
     */
    public void addMoney(int amount) { if (amount > 0) money += amount; }

    /**
     * Deducts money from the player's balance.
     * @param amount The amount of money to deduct.
     */
    public void deductMoney(int amount) { if (money > 0) money -= amount; }

    /**
     * Increments the player's jail turns by one.
     */
    public void incrementJailTurns() { jailTurns++; }

    /**
     * Adds a property to the player's list of owned properties.
     * @param property The property to add.
     */
    public void addProperty(PropertySquare property) { properties.add(property); }

    /**
     * Removes a property from the player's list of owned properties.
     * @param property The property to remove.
     */
    public void removeProperty(PropertySquare property) { properties.remove(property); }
}
