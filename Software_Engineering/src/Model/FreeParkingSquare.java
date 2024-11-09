package Model;

public class FreeParkingSquare extends Square {
    public FreeParkingSquare(String name) {
        super(name);
    }

    @Override
    public void landOn(Player player) {
        System.out.println(player.getName() + " landed on Free Parking. Nothing happens.");
    }
}
