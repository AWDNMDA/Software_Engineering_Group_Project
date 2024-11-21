package Model;

/**
 * Represents the "Go" square in the game.
 * Players passing or landing on this square receive a fixed monetary reward.
 */
public class GoSquare extends Square {

    /**
     * Constructor for the GoSquare class.
     * @param name The name of the square (e.g., "Go").
     */
    public GoSquare(String name) {
        super(name);
    }

    /**
     * Action performed when a player lands on or passes this square.
     * Adds HKD 1500 to the player's total money.
     * @param player The player who lands on the square.
     */
    @Override
    public void landOn(Player player) {
        player.setMoney(player.getMoney() + 1500);
        System.out.println(player.getName() + " passed Go and received HKD 1500.");
    }
}
