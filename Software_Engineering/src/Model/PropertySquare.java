package Model;

import java.util.Scanner;

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
        if (owner == null) { // Property is unowned
            System.out.println(player.getName() + ", you landed on " + getName() + ". This property costs HKD " + price + ".");
            if (player.getMoney() >= price) {
                System.out.println("Would you like to buy this property? (Y/N)");
                Scanner scanner = new Scanner(System.in);
                String choice = scanner.nextLine().trim().toUpperCase();

                if (choice.equals("Y")) {
                    player.setMoney(player.getMoney() - price);
                    player.addProperty(this);
                    this.owner = player;
                    System.out.println(player.getName() + " bought " + getName() + " for HKD " + price + "!");
                } else {
                    System.out.println(player.getName() + " chose not to buy " + getName() + ".");
                }
            } else {
                System.out.println("You do not have enough money to buy this property.");
            }
        } else if (!owner.equals(player)) { // Property is owned by another player
            player.setMoney(player.getMoney() - rent);
            owner.setMoney(owner.getMoney() + rent);
            System.out.println(player.getName() + " paid HKD " + rent + " rent to " + owner.getName());
        } else {
            System.out.println(player.getName() + ", you landed on your own property.");
        }
    }

    // Getters and setters
    public int getPrice() {
        return price;
    }

    public int getRent() {
        return rent;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRent(int rent) {
        this.rent = rent;
    }

    public void setName(String name) {
        super.name = name;
    }
    public void clearOwner() {
        owner=null;
    }

}
