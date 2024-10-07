import controller.GameController;
import javafx.stage.Stage;
import model.Ball;
import model.Game;
import model.Player;
import model.Racket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GameCanvas;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Racket test.
 */
public class RacketTest {
    private GameController gameController;
    private Game game;
    private Ball ball;
    private GameCanvas gameCanvas;
    private Stage primaryStage;

    /**
     * Sets up.
     */
    @BeforeEach
    public void setUp() throws SQLException {
        gameController = new GameController(primaryStage);
        gameCanvas = new GameCanvas(gameController);
        game = gameController.getGame();
        gameCanvas.setWidth(600);
        gameCanvas.setHeight(400);

        Player player1 = new Player(1, "Player 1", new Racket(50, 50, 20, 90, game), game);
        Player player2 = new Player(2, "Player 2", new Racket(550, 50, 20, 90, game), game);

        ball = game.getBall();
    }

    /**
     * Test move racket up.
     */
    @Test
    public void testMoveRacketUp() {
        Racket racket = new Racket(50, 50, 20, 90, game);
        double initialY = racket.getPosY();
        racket.moveUp();

        // checking if racket moved up
        assertEquals(initialY - 10, racket.getPosY(), 0.01);
    }

    /**
     * Test move racket down.
     */
    @Test
    public void testMoveRacketDown() {
        Racket racket = new Racket(50, 50, 20, 90, game);
        double initialY = racket.getPosY();
        racket.moveDown();

        // checking if racket moved down
        assertEquals(initialY + 10, racket.getPosY(), 0.01);
    }

    /**
     * Test racket ball collision.
     */
    @Test
    public void testRacketBallCollision() {
        Racket racket = new Racket(50, 50, 20, 90, game);
        Ball ball = new Ball(game, 60, 70, 10, 0, 0, 0);

        assertTrue(racket.collidesWithBall(ball,  gameCanvas.getWidth(),  gameCanvas.getHeight()));
    }

    /**
     * Test racket ball no collision.
     */
    @Test
    public void testRacketBallNoCollision() {
        Racket racket = new Racket(50, 50, 20, 90, game);
        Ball ball = new Ball(null, 10, 10, 10, 0, 0, 0);

        assertFalse(racket.collidesWithBall(ball, gameCanvas.getWidth(),  gameCanvas.getHeight()));
    }

}