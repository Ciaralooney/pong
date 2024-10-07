package controller;


import view.GameCanvas;
import javafx.stage.Stage;
import model.Game;
import model.Ball;

import java.sql.SQLException;

/**
 * The type Game controller.
 */
public class GameController {
    private Game game;
    private MenuListener menuListener;
    private GameCanvas gameCanvas;
    private Ball ball;

    private BallManager ballManager;
    private Stage stage;

    /**
     * Instantiates a new Game controller.
     *
     * @param primaryStage the primary stage
     */
    public GameController(Stage primaryStage) throws SQLException {

        this.gameCanvas = new GameCanvas(this);
        this.game = new Game(gameCanvas);
        this.ball = game.getBall();
        this.game.setBall(ball);
        this.menuListener = new MenuListener(this,  primaryStage, gameCanvas,game);
        this.ballManager = new BallManager(game, gameCanvas);
        Thread ballManagerThread = new Thread(ballManager);
        ballManagerThread.setDaemon(true);
        ballManagerThread.start();
        //menuListener.pauseKeyListeners();
    }

    /**
     * Sets stage.
     *
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Gets ball manager.
     *
     * @return the ball manager
     */
    public BallManager getBallManager() {
        return ballManager;
    }


    /**
     * Gets game canvas.
     *
     * @return the game canvas
     */
    public GameCanvas getGameCanvas() {
        return gameCanvas;
    }

    /**
     * Gets game.
     *
     * @return the game
     */
    public Game getGame() {
        return game;
    }

    /**
     * Gets menu listener.
     *
     * @return the menu listener
     */
    public MenuListener getMenuListener() {
        return menuListener;
    }

    /**
     * Sets game.
     *
     * @param game the game
     */
    public void setGame(Game game) {
        this.game = game;
    }


}
