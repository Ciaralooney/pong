import model.Racket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.stage.Stage;

import controller.BallManager;
import controller.GameController;
import model.Game;
import model.Player;
import view.GameCanvas;

import static org.junit.jupiter.api.Assertions.*;


/**
 * The type Ball manager test.
 */
public class BallManagerTest {

    private GameController gameController;
    private Game game;
    private Player player;
    private GameCanvas gameCanvas;
    private BallManager ballManager;
    private Stage primaryStage;


    @BeforeEach
    public void setUp() throws Exception {
        // creating instances needed for testing
        gameCanvas.setWidth(600);
        gameCanvas.setHeight(400);
        gameController = new GameController(primaryStage);
        this.gameController.setStage(primaryStage);
        game = gameController.getGame();
        gameCanvas = gameController.getGameCanvas();
        ballManager = new BallManager(game, gameCanvas);
        Player player1 = new Player(1, "Player 1", new Racket(50, 50, 20, 90, game), game);
        Player player2 = new Player(2, "Player 2", new Racket(550, 50, 20, 90, game), game);

    }

    @Test
    public void testMoveBall() {
            // finding initial ball positions
            double initialX = game.getBall().getX();
            double initialY = game.getBall().getY();

            ballManager.moveBall();

            // finding new ball positions
            double newX = game.getBall().getX();
            double newY = game.getBall().getY();

            // checking balls have moved
            assertNotEquals(initialX, newX);
            assertNotEquals(initialY, newY);
    }


    @Test
    public void testPauseAndResumeBall() {
        ballManager.pauseBall();
        assertTrue(ballManager.isPaused());

        ballManager.resumeBall();
        assertFalse(ballManager.isPaused());
    }

    @Test
    public void testCheckRacketCollisions_NoCollision() {
        // testing for no collisions
        ballManager.checkRacketCollisions();
        assertEquals(0, game.getBall().getSpeedX());
    }

    @Test
    public void testCheckRacketCollisions_Collision() {
        // testing for a collision
        ballManager.checkRacketCollisions();
        assertNotEquals(0, game.getBall().getSpeedX());
    }

    @Test
    public void testResetBallPosition() {
        ballManager.resetBallPosition();

        // checking if it has reset
        assertEquals(100, game.getBall().getX());
        assertEquals(100, game.getBall().getY());
    }


}