package Controller;
import Model.Game;
import Model.Player;
import View.*;

import java.util.List;

public class GameController {
    private final Game game;
    private final Display view;

    public GameController(Game game, Display view) {
        this.game = game;
        this.view = view;
    }

    public void startGame() {
        System.out.println();
        view.displayBoard(game.getBoard());
        while (!game.isGameOver()) {
            playTurn();
        }
        declareWinner();
    }

    private void playTurn() {
        int currentRound = game.getRoundCount();
        Player currentPlayer = game.getCurrentPlayer();
        view.displayPlayerTurn(currentRound, currentPlayer);

        game.playTurn();
        view.displayGameStatus(game.getPlayers());
    }

    private void declareWinner() {
        List<Player> winners = game.getWinner();
        view.displayWinner(winners);
    }
}

