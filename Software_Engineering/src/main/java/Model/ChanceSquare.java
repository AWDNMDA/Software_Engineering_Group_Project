package Model;

import java.util.Random;

/**
 * Represents a "Chance" square in the game where players can gain or lose money
 * based on random events. The square introduces an element of unpredictability
 * to the game, enhancing player engagement and strategy.
 */
public class ChanceSquare extends Square {
    private final Random random;

    /**
     * Constructor for a ChanceSquare with a custom Random generator.
     * Useful for testing or injecting a specific source of randomness.
     * @param name   The name of the square.
     * @param random A Random object to control the randomness.
     */
    public ChanceSquare(String name, Random random) {
        super(name);
        this.random = random;
    }

    /**
     * Constructor for a ChanceSquare with a default Random generator.
     * @param name The name of the square.
     */
    public ChanceSquare(String name) {
        super(name);
        this.random = new Random();
    }

    /**
     * Method called when a player lands on this Chance square.
     * A random amount (multiple of 10, between 10 and 300) is either added to or deducted from
     * the player's money. Whether the player gains or loses money is also determined randomly.
     * @param player The player who lands on the square.
     */
    @Override
    public void landOn(Player player) {
        // Generate a random amount (10 to 300, in multiples of 10)
        int amount = (random.nextInt(31) + 1) * 10;
        boolean gain = random.nextBoolean();
        if (gain) {
            player.addMoney(amount);
            System.out.println(player.getName() + " gained HKD " + amount + " from Chance.");
        } else {
            player.deductMoney(amount);
            System.out.println(player.getName() + " lost HKD " + amount + " from Chance.");
        }
    }
}
