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
 * The type Game test.
 */
public class GameTest {

    private GameController gameController;
    private Game game;
    private Player player;
    private GameCanvas gameCanvas;
    private BallManager ballManager;
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

    }

    /**
     * Test end game player 2 wins.
     */
    @Test
    public void testEndGame_Player2Wins() {
        // Set up initial conditions where player 2 wins
        game.getPlayer(2).setScore(10);
        game.getPlayer(1).setScore(7);
        game.setLimit(10);

        // Call the endGame method
        game.endGame();

        assertEquals(2, game.endGame()); // checking player 2 won
    }

    /**
     * Test end game player 1 wins.
     */
    @Test
    public void testEndGame_Player1Wins() {
        // Set up initial conditions where player 1 wins
        game.getPlayer(1).setScore(10);
        game.getPlayer(2).setScore(2);
        game.setLimit(10);

        // Call the endGame method
        game.endGame();

        assertEquals(2, game.endGame()); // checking player 1 won
    }

    /**
     * Test ball racket collision.
     */
    @Test
    public void testBallRacketCollision() {
        // set up initial conditions for ball and racket positions
        game.getBall().setX(100);
        game.getBall().setY(100);
        game.getPlayer(1).getRacket().setPosX(50);
        game.getPlayer(1).getRacket().setPosY(100);
        game.getBall().setSize(10);
        game.getPlayer(1).getRacket().setWidth(20);
        game.getPlayer(1).getRacket().setHeight(90);

        // ball movement towards racket
        game.getBall().setSpeedX(5);  // Set ball speed
        game.getBall().move();  // Move the ball

        // checking if collision is detected
        assertTrue(game.getBall().collidesWithRacket(game.getPlayer(1).getRacket()));
    }
}