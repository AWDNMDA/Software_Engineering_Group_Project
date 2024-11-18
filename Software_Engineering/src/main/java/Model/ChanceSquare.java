package Model;

import java.util.Random;

public class ChanceSquare extends Square {
    private final Random random;

    public ChanceSquare(String name, Random random) {
        super(name);
        this.random = random;
    }

    public ChanceSquare(String name) {
        super(name);
        this.random = new Random();
    }

    @Override
    public void landOn(Player player) {
        int amount = (random.nextInt(31) + 1) * 10; // Random multiple of 10 up to 300
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