package Model;

public abstract class Square {
    private String name;

    public Square(String name) {
        this.name = name;
    }

    public abstract void landOn(Player player);

    public String getName() {
        return name;
    }
}