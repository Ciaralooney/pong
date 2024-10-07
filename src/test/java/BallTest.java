import controller.GameController;
import javafx.stage.Stage;
import model.Ball;
import model.Game;
import model.Player;
import model.Racket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import view.GameCanvas;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The type Ball test.
 */
public class BallTest {
    private GameController gameController;
    private Game game;
    private Ball ball;
    private GameCanvas gameCanvas;
    private Stage primaryStage;

    /**
     * Sets up.
     */
    @BeforeEach
    public void setUp() {
        gameController = new GameController(primaryStage);
        gameCanvas = new GameCanvas(gameController);
        game = new Game(gameCanvas);
        gameCanvas.setWidth(600);
        gameCanvas.setHeight(400);

        Player player1 = new Player(1, "Player 1", new Racket(50, 50, 20, 90, game), game);
        Player player2 = new Player(2, "Player 2", new Racket(550, 50, 20, 90, game), game);

        ball = game.getBall();
    }

    /**
     * Test ball collides with top wall.
     */
    @Test
    public void testBallCollidesWithTopWall() {
        // ball collides with the top wall
        ball.setX(100);
        ball.setY(5);
        ball.setSize(10);
        ball.setSpeedY(-5);  // moving ball up
        ball.move();

        // checking collision
        assertTrue(ball.getY() > 0);  // ball should have rebounded
    }

    /**
     * Test ball collides with bottom wall.
     */
    @Test

    public void testBallCollidesWithBottomWall() {
        // ball collides with the bottom wall
        double canvasHeight = game.getGameCanvas().getHeight();
        ball.setX(100);
        ball.setY(1);
        ball.setSize(10);
        ball.setSpeedY(5);  // moving ball down
        ball.move();

        // checking collision
        assertTrue(ball.getY() < canvasHeight - ball.getSize());  // ball should have rebounded
    }

    /**
     * Test ball collides with racket.
     */
    @Test
    public void testBallCollidesWithRacket() {
        // ball collides with a racket
        ball.setX(100);
        ball.setY(100);
        ball.setSize(10);
        ball.setSpeedX(-5);
        ball.move();

        // checking collision
        assertTrue(ball.collidesWithRacket(game.getPlayer(1).getRacket()));
    }

}
