package Controller;
import Model.Game;
import Model.Player;
import View.*;

import java.util.List;

public class GameController {
    private Game game;
    private Display view;

    public GameController(Game game, Display view) {
        this.game = game;
        this.view = view;
    }

    public void startGame() {
        view.displayBoard(game.getBoard());
        while (!game.isGameOver()) {
            nextTurn();
        }
        List<Player> winner = game.getWinner();
        view.displayWinner(winner);
        game.getBoard().resetBoard();
    }

    public void nextTurn() {
        game.playTurn();
        view.displayGameStatus(game.getPlayers());
    }


}

