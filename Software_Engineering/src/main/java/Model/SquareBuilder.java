package Model;

public class SquareBuilder {
    public static Square createSquare(String type, String name) {
        return switch (type) {
            case "Go" -> new GoSquare(name);
            case "FreeParking" -> new FreeParkingSquare(name);
            case "GoToJail" -> new GoToJailSquare(name);
            case "IncomeTax" -> new IncomeTaxSquare(name);
            case "Chance" -> new ChanceSquare(name);
            default -> throw new IllegalArgumentException("Unknown square type: " + type);
        };
    }

    public static Square createSquare(String type, String name, int price, int rent) {
        if (type.equals("Property")) {
            return new PropertySquare(name, price, rent);
        }
        throw new IllegalArgumentException("Invalid property square type: " + type);
    }
}
