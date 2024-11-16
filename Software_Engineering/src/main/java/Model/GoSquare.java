package Model;

public class GoSquare extends Square {
    public GoSquare(String name) {
        super(name);
    }
    @Override
    public void landOn(Player player) {
        player.setMoney(player.getMoney() + 1500);
        System.out.println(player.getName() + " passed Go and received HKD 1500.");
    }
}