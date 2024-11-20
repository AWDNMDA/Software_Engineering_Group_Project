package Model;

public class GoToJailSquare extends Square {
    private final int jailPosition;

    public GoToJailSquare(String name, int jailPosition) {
        super(name);
        this.jailPosition = jailPosition;
    }

    @Override
    public void landOn(Player player) {
        player.setPosition(jailPosition);
        player.setInJail(true);
        System.out.println(player.getName() + " goes to Jail.");
    }
}
