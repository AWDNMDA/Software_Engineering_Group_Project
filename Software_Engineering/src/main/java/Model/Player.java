package Model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String name;
    private int money;
    private int position;
    private boolean inJail;
    private int jailTurns;
    private final List<PropertySquare> properties;

    public Player(String name) {
        this.name = name;
        this.money = 1500;
        this.position = 0;
        this.inJail = false;
        this.jailTurns = 0;
        this.properties = new ArrayList<>();
    }


    public String getName(){
        return name;
    }
    public int getMoney(){
        return money;
    }
    public int getPosition(){
        return position;
    }
    public boolean isInJail(){return inJail;}
    public List<PropertySquare> getProperties(){return properties;}
    public int getJailTurns(){return jailTurns;}

    public void setMoney(int money) { this.money = money; }
    public void setPosition(int position) { this.position = position; }
    public void setInJail(boolean inJail) { this.inJail = inJail; }
    public void setJailTurns(int turns) { this.jailTurns = turns; }


    public void move(int steps) {
        position = (position + steps) % 20;
    }
    public void addMoney(int amount) {if (amount > 0) money += amount;}
    public void deductMoney(int amount){if(money > 0) money -= amount;}


    public void incrementJailTurns() { jailTurns++; }
    public void addProperty(PropertySquare property) { properties.add(property); }
    public void removeProperty(PropertySquare property) { properties.remove(property); }
}
