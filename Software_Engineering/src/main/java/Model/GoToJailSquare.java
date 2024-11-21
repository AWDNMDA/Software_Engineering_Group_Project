package Model;

/**
 * Represents a "Go to Jail" square in the game.
 * Players landing on this square are sent to jail and their position is updated.
 */
public class GoToJailSquare extends Square {
    private final int jailPosition;

    /**
     * Constructor for the GoToJailSquare class.
     * @param name         The name of the square (e.g., "Go to Jail").
     * @param jailPosition The index of the Jail square on the board.
     */
    public GoToJailSquare(String name, int jailPosition) {
        super(name);
        this.jailPosition = jailPosition;
    }

    /**
     * Action performed when a player lands on this square.
     * The player is sent to jail and their position is updated accordingly.
     * @param player The player who lands on the square.
     */
    @Override
    public void landOn(Player player) {
        player.setPosition(jailPosition);
        player.setInJail(true);
        System.out.println(player.getName() + " goes to Jail.");
    }
}
