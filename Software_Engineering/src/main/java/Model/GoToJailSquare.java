package Model;

public class GoToJailSquare extends Square {
    public GoToJailSquare(String name) {
        super(name);
    }

    @Override
    public void landOn(Player player) {
        player.setPosition(5); // Assuming position 5 is "In Jail/Just Visiting"
        player.setInJail(true);
        System.out.println(player.getName() + " goes to Jail.");
    }
}