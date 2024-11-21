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

    public boolean isOwned() { return owner != null; }
    public Player getOwner() { return owner; }
    public int getPrice() { return price; }
    public int getRent() { return this.rent; }

    public void setName(String name) { super.setName(name); }
    public void setOwner(Player owner) { this.owner = owner; }
    public void setPrice(int price) {
        if (price > 0) {
            this.price = price;
        } else {
            throw new IllegalArgumentException("Price must be greater than 0.");
        }
    }
    public void setRent(int rent) {
        if (rent > 0) {
            this.rent = rent;
        } else {
            throw new IllegalArgumentException("Rent must be greater than 0.");
        }
    }

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
        } else {
            System.out.println("You own this property.");
        }
    }

}
