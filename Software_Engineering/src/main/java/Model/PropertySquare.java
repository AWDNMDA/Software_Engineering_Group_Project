package Model;

/**
 * Represents a property square on the game board.
 * A property square can be purchased, owned, and rented by players.
 * Players landing on the square may need to pay rent if the property is owned by another player.
 */
public class PropertySquare extends Square {
    private int price;
    private int rent;
    private Player owner;

    /**
     * Constructor to initialize the property square.
     * @param name  The name of the property.
     * @param price The purchase price of the property.
     * @param rent  The rent charged to players landing on the property.
     */
    public PropertySquare(String name, int price, int rent) {
        super(name);
        this.price = price;
        this.rent = rent;
        this.owner = null;
    }

    /**
     * Checks if the property is owned.
     * @return True if the property has an owner, false otherwise.
     */
    public boolean isOwned() { return owner != null; }

    /**
     * Retrieves the owner of the property.
     * @return The player who owns the property, or null if unowned.
     */
    public Player getOwner() { return owner; }

    /**
     * Retrieves the price of the property.
     * @return The purchase price of the property.
     */
    public int getPrice() { return price; }

    /**
     * Retrieves the rent charged for the property.
     * @return The rent amount.
     */
    public int getRent() { return rent; }

    /**
     * Updates the name of the property.
     * @param name The new name of the property.
     */
    public void setName(String name) { super.setName(name); }

    /**
     * Sets the owner of the property.
     * @param owner The player who now owns the property.
     */
    public void setOwner(Player owner) { this.owner = owner; }

    /**
     * Updates the price of the property.
     * @param price The new price of the property. Must be greater than 0.
     * @throws IllegalArgumentException If the price is less than or equal to 0.
     */
    public void setPrice(int price) {
        if (price > 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
    }

    /**
     * Updates the rent of the property.
     * @param rent The new rent amount. Must be greater than 0.
     * @throws IllegalArgumentException If the rent is less than or equal to 0.
     */
    public void setRent(int rent) {
        if (rent > 0) {
            this.rent = rent;
        } else {
            throw new IllegalArgumentException("Rent must be greater than 0.");
        }
    }

    /**
     * Allows a player to purchase the property.
     * @param player The player attempting to buy the property.
     */
    public void buyProperty(Player player) {
        if (!isOwned() && player.getMoney() >= price) {
            player.deductMoney(price);
            owner = player;
            player.addProperty(this);
            System.out.println(player.getName() + " bought " + getName() + " for HKD " + price);
        } else {
            System.out.println(player.getName() + " cannot afford " + getName());
        }
    }

    /**
     * Handles the action when a player lands on this square.
     * @param player The player landing on the square.
     */
    @Override
    public void landOn(Player player) {
        if (isOwned() && owner != player) {
            player.deductMoney(rent);
            owner.addMoney(rent);
            System.out.println(player.getName() + " paid HKD " + rent + " rent to " + owner.getName());
        } else if (!isOwned()) {
            System.out.println(getName() + " is available for purchase at HKD " + price);
        } else {
            System.out.println("You own this property.");
        }
    }
}
