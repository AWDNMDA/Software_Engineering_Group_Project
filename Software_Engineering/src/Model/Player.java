package Model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int money;
    private int position;
    private boolean inJail;
    private int jailTurns;
    private List<PropertySquare> properties;

    public Player(String name) {
        this.name = name;
        this.money = 1500;
        this.position = 0;
        this.inJail = false;
        this.jailTurns = 0;
        this.properties = new ArrayList<>();
    }

    // Getters and setters

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
    public void setJailTurns(int turn){
        this.jailTurns = turn;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setMoney(int money){
        this.money = money;
    }
    public void setPosition(int position){
        this.position = position;
    }
    public void setInJail(boolean inJail){
        this.inJail = inJail;
    }


    public void move(int steps) {
        position = (position + steps) % 20;
    } // 20 is a cycle

    public void addProperty(PropertySquare property) {
        properties.add(property);
    }

    public void removeProperty(PropertySquare property) {
        properties.remove(property);
    }
}
