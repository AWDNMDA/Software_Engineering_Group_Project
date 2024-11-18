package Model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final List<Square> squares;

    public Board() {
        squares = new ArrayList<>();
        initializeSquares();
    }

    private void initializeSquares() {
        squares.add(SquareBuilder.createSquare("Go", "Go"));
        squares.add(SquareBuilder.createSquare("Property", "Central", 800, 90));
        squares.add(SquareBuilder.createSquare("IncomeTax", "Income Tax"));
        squares.add(SquareBuilder.createSquare("Property", "Wan Chai", 700, 65));
        squares.add(SquareBuilder.createSquare("FreeParking", "Free Parking"));
        squares.add(SquareBuilder.createSquare("GoToJail", "Go To Jail"));
        squares.add(SquareBuilder.createSquare("Property", "Stanley", 600, 60));
        squares.add(SquareBuilder.createSquare("Chance", "Chance"));
        squares.add(SquareBuilder.createSquare("Property", "Shek O", 400, 10));
        squares.add(SquareBuilder.createSquare("Property", "Mong Kok", 500, 40));
        squares.add(SquareBuilder.createSquare("Property", "Tsing Yi", 400, 15));
        squares.add(SquareBuilder.createSquare("Chance", "Chance"));
        squares.add(SquareBuilder.createSquare("Property", "Shatin", 700, 75));
        squares.add(SquareBuilder.createSquare("Property", "Tuen Mun", 400, 20));
        squares.add(SquareBuilder.createSquare("Property", "Tai Po", 500, 25));
        squares.add(SquareBuilder.createSquare("Property", "Sai Kung", 400, 10));
        squares.add(SquareBuilder.createSquare("Property", "Yuen Long", 400, 25));
        squares.add(SquareBuilder.createSquare("Property", "Tai O", 600, 25));
        squares.add(SquareBuilder.createSquare("FreeParking", "Free Parking"));
        squares.add(SquareBuilder.createSquare("Chance", "Chance"));
    }

    public Square getSquare(int position) {
        return squares.get(position % squares.size());
    }
    public int getTotalSquares() { return squares.size(); }
}