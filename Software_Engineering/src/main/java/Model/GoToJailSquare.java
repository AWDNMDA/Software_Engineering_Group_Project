package Model;

public class GoToJailSquare extends Square {
    public GoToJailSquare(String name) {
        super(name);
    }

    @Override
    public void landOn(Player player) {
        player.setPosition(5);
        player.setInJail(true);
        System.out.println(player.getName() + " goes to Jail.");
    }
}