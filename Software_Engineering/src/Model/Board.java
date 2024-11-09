package Model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Square> squares;

    public Board() {
        squares = new ArrayList<>();
        initializeSquares();
    }

    private void initializeSquares() {
        squares.add(new GoSquare("Go")); // 0
        squares.add(new PropertySquare("Central", 800, 90)); // 1
        squares.add(new IncomeTaxSquare("Income Tax")); // 2
        squares.add(new PropertySquare("Wan Chai", 700, 65)); // 3
        squares.add(new FreeParkingSquare("Free Parking")); // 4
        squares.add(new GoToJailSquare("Go To Jail")); // 5
        squares.add(new PropertySquare("Stanley", 600, 60)); // 6
        squares.add(new ChanceSquare("Chance")); // 7
        squares.add(new PropertySquare("Shek O", 400, 10)); // 8
        squares.add(new PropertySquare("Mong Kok", 500, 40)); // 9
        squares.add(new PropertySquare("Tsing Yi", 400, 15)); // 10
        squares.add(new ChanceSquare("Chance")); // 11
        squares.add(new PropertySquare("Shatin", 700, 75)); // 12
        squares.add(new PropertySquare("Tuen Mun", 400, 20)); // 13
        squares.add(new PropertySquare("Tai Po", 500, 25)); // 14
        squares.add(new PropertySquare("Sai Kung", 400, 10)); // 15
        squares.add(new PropertySquare("Yuen Long", 400, 25)); // 16
        squares.add(new PropertySquare("Tai O", 600, 25)); // 17
        squares.add(new FreeParkingSquare("Free Parking")); // 18
        squares.add(new ChanceSquare("Chance")); // 19
    }

    public Square getSquare(int position) {
        return squares.get(position);
    }
}