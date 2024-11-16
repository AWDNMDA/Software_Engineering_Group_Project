package Model;

public class IncomeTaxSquare extends Square {
    public IncomeTaxSquare(String name) {
        super(name);
    }

    @Override
    public void landOn(Player player) {
        int tax = (int) (player.getMoney() * 0.1);
        // player.setMoney(player.getMoney() - tax);
        player.deductMoney(tax);
        System.out.println(player.getName() + " paid HKD " + tax + " as income tax.");
    }
}