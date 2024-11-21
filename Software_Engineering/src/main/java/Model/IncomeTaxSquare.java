package Model;

/**
 * Represents an "Income Tax" square in the game.
 * Players landing on this square pay 10% of their total money as tax.
 */
public class IncomeTaxSquare extends Square {

    /**
     * Constructor for the IncomeTaxSquare class.
     * @param name The name of the square (e.g., "Income Tax").
     */
    public IncomeTaxSquare(String name) {
        super(name);
    }

    /**
     * Action performed when a player lands on this square.
     * Deducts 10% of the player's total money as tax.
     * @param player The player who lands on the square.
     */
    @Override
    public void landOn(Player player) {
        int tax = (int) (player.getMoney() * 0.1);
        player.deductMoney(tax);
        System.out.println(player.getName() + " paid HKD " + tax + " as income tax.");
    }
}
