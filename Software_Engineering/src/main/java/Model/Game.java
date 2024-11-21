    package Model;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Random;
    import java.util.Scanner;
    import java.util.stream.Collectors;

    public class Game {
        private static final int MAX_ROUNDS = 100;
        private static final int JAIL_FINE = 150;

        private final List<Player> players;
        private final Board board;
        private int currentPlayerIndex;
        private int roundCount;
        private final Random random;
        private final Scanner scanner;

        public Game(List<Player> players, Board board, Random random, Scanner scanner) {
            this.players = new ArrayList<>(players);
            this.board = board;
            this.random = random;
            this.scanner = scanner;
            this.currentPlayerIndex = 0;
            this.roundCount = 0;
        }

        public Game(List<Player> players, Board board) {
            this(players, board, new Random(), new Scanner(System.in));
        }

        public List<Player> getPlayers(){ return players; }
        public Player getCurrentPlayer() { return players.get(currentPlayerIndex); }
        public Board getBoard(){return board;}
        public int getRoundCount() { return roundCount; }
        public boolean isGameOver() { return players.size() <= 1 || roundCount >= MAX_ROUNDS; }
        public List<Player> getWinner() {
            int maxMoney = players.stream().mapToInt(Player::getMoney).max().orElse(0);
            return players.stream()
                    .filter(p -> p.getMoney() == maxMoney)
                    .collect(Collectors.toList());
        }


        public void playTurn() {
            if (isGameOver()) {
                System.out.println("Game is over!");
                return;
            }

            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer.isInJail()) {
                handleJail(currentPlayer);
            } else {
                takeTurn(currentPlayer);
            }

            checkBankruptcy();
            nextPlayer();
        }

        private void takeTurn(Player player) {
            System.out.println(player.getName() + ", it's your turn. Press Enter to roll the dice.");
            scanner.nextLine();
            int diceRoll = rollDice();
            System.out.println(player.getName() + " rolled " + diceRoll);
            movePlayer(player, diceRoll);
        }

        private void movePlayer(Player player, int diceRoll) {
            player.move(diceRoll);
            Square square = board.getSquare(player.getPosition());
            System.out.println(player.getName() + " landed on: " + square.getName());

            if (square instanceof GoToJailSquare goToJailSquare) {
                goToJailSquare.landOn(player);
            } else if (square instanceof PropertySquare propertySquare) {
                handleProperty(player, propertySquare);
            } else {
                square.landOn(player);
            }
        }

        private void nextPlayer() {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            roundCount++;
        }

        private void checkBankruptcy() { players.removeIf(player -> player.getMoney() < 0); }


        private void handleProperty(Player player, PropertySquare propertySquare) {
            if (!propertySquare.isOwned()) {
                System.out.println(propertySquare.getName() + " is available for purchase at HKD " + propertySquare.getPrice() + ".");
                System.out.println("Would you like to buy it? (yes/no)");

                String choice = getValidatedInput("yes", "no");
                switch (choice) {
                    case "yes" -> {
                        if (player.getMoney() >= propertySquare.getPrice()) {
                            propertySquare.buyProperty(player);
                        } else {
                            System.out.println("You do not have enough money to buy this property!");
                        }
                    }
                    case "no" -> System.out.println("You chose not to buy the property.");
                    default -> System.out.println("Invalid input. Please enter 'yes' or 'no'.");
                }
            } else if (!propertySquare.getOwner().equals(player)) {
                propertySquare.landOn(player);
            } else {
                System.out.println("You own this property.");
            }
        }

        private void handleJail(Player player) {
            System.out.println(player.getName() + " is in jail.");

            if (player.getJailTurns() < 2) {
                String choice = getValidatedInput("Press 'T' to pay HKD " + JAIL_FINE +
                        " and get out, or 'F' to roll for doubles.", "T", "F");
                switch (choice) {
                    case "T" -> payFineAndMove(player);
                    case "F" -> rollForDoubles(player);
                }
            } else {
                System.out.println("You must pay HKD " + JAIL_FINE + " to get out of jail.");
                payFineAndMove(player);
            }
        }

        private void payFineAndMove(Player player) {
            if (player.getMoney() >= JAIL_FINE) {
                player.deductMoney(JAIL_FINE);
                player.setInJail(false);
                System.out.println(player.getName() + " paid HKD " + JAIL_FINE + " and is now out of jail.");
            } else {
                System.out.println(player.getName() + " does not have enough money to pay the fine!");
                checkBankruptcy();
            }
        }

        private void rollForDoubles(Player player) {
            System.out.println(player.getName() + " is rolling for doubles...");
            int die1 = random.nextInt(6) + 1;
            int die2 = random.nextInt(6) + 1;

            System.out.println("You rolled a " + die1 + " and a " + die2);
            if (die1 == die2) {
                System.out.println("You rolled doubles and are out of jail!");
                player.setInJail(false);
                player.setJailTurns(0);
                movePlayer(player, die1 + die2);
            } else {
                System.out.println("No doubles. Jail turns incremented.");
                player.incrementJailTurns();
            }
        }

        int rollDice() {
            return random.nextInt(6) + 1 + random.nextInt(6) + 1;
        }

        private String getValidatedInput(String... validOptions) {
            while (true) {
                String input = scanner.nextLine().trim();
                for (String option : validOptions) {
                    if (option.equals(input)) {
                        return input;
                    }
                }
                System.out.println("Invalid input. Please try again.");
            }
        }
    }