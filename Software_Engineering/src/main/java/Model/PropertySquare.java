package Model;

public class PropertySquare extends Square {
    private final int price;
    private final int rent;
    private Player owner;

    public PropertySquare(String name, int price, int rent) {
        super(name);
        this.price = price;
        this.rent = rent;
        this.owner = null;
    }

    public boolean isOwned() { return owner != null; }
    public Player getOwner() { return owner; }
    public int getPrice() { return price; }

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

    @Override
    public void landOn(Player player) {
        if (isOwned() && owner != player) {
            player.deductMoney(rent);
            owner.addMoney(rent);
            System.out.println(player.getName() + " paid HKD " + rent + " rent to " + owner.getName());
        } else if (!isOwned()) {
            System.out.println(getName() + " is available for purchase at HKD " + price);
        }
    }
}
