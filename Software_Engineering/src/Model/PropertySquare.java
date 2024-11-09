package Model;

public class PropertySquare extends Square {
    private int price;
    private int rent;
    private Player owner;

    public PropertySquare(String name, int price, int rent) {
        super(name);
        this.price = price;
        this.rent = rent;
        this.owner = null;
    }

    @Override
    public void landOn(Player player) {
        if (owner == null) {
            // Logic to buy property
        } else if (!owner.equals(player)) {
            // Pay rent
        }
    }

    // Getters and setters
}
