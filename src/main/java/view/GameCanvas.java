package view;

import controller.GameController;
import controller.MenuListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Game;
import model.Player;

import java.io.Serializable;

/**
 * The type Game canvas.
 */
public class GameCanvas extends Canvas implements Serializable {
    private MenuListener menuListener;
    private Game game;

    private double racketWidth;
    private double racketHeight;
    private int ballSpeedFrequency = 1;
    private double ballSpeedX;
    private double ballSpeedY;

    /**
     * Instantiates a new Game canvas.
     * At the end it calls the redraw function to draw the objects on the canvas
     *
     * @param gameController the game controller
     */
    public GameCanvas(GameController gameController) {
        this.menuListener = gameController.getMenuListener();
        this.game = new Game(this);
        // initial size of the canvas
        setWidth(600);
        setHeight(400);
        this.racketWidth = getWidth() / 30;
        this.racketHeight = getHeight() / 3;

        widthProperty().addListener((observable, oldValue, newValue) -> redraw(game));
        heightProperty().addListener((observable, oldValue, newValue) -> redraw(game));

        redraw(game);
    }

    /**
     * Calling functions to draw all elements on the canvas
     * This includes rackets, ball, and player scores.
     *
     * @param game the current game state.
     */
    public void redraw(Game game) {

        this.game=game;
        // updating racket positions and width/height when canvas is redrawn
        game.getPlayer(1).getRacket().setY(game.getPlayer(1).getRacket().getPosY());
        game.getPlayer(1).getRacket().setX(game.getPlayer(1).getRacket().getPosX());

        game.getPlayer(2).getRacket().setY(game.getPlayer(2).getRacket().getPosY());
        game.getPlayer(2).getRacket().setX(game.getPlayer(2).getRacket().getPosX());

        game.getPlayer(1).getRacket().setWidth(game.getPlayer(1).getRacket().getWidth());
        game.getPlayer(1).getRacket().setHeight(game.getPlayer(1).getRacket().getHeight());

        game.getPlayer(2).getRacket().setWidth(game.getPlayer(2).getRacket().getWidth());
        game.getPlayer(2).getRacket().setHeight(game.getPlayer(2).getRacket().getHeight());

        GraphicsContext gc = getGraphicsContext2D();

        double canvasWidth = getWidth();
        double canvasHeight = getHeight();

        gc.clearRect(0, 0, canvasWidth, canvasHeight);

        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, getWidth(), getHeight());

        drawRacket(gc, canvasWidth, game.getPlayer(1).getRacket().getWidth(), game.getPlayer(1).getRacket().getHeight(), 1);
        drawRacket(gc, canvasWidth, game.getPlayer(2).getRacket().getWidth(), game.getPlayer(2).getRacket().getHeight(), 2);

        drawPlayerText(gc, canvasWidth, game.getPlayer(1));
        drawPlayerText(gc, canvasWidth, game.getPlayer(2));

        Platform.runLater(()-> drawBall(gc, canvasWidth, game));

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
     * Sets game.
     *
     * @param game  The current state of the game.
     */
    public void setGame(Game game) {
        this.game = game;
    }


    /**
     * Draws the ball on the canvas ensuring it is the up-to-date size and position
     *
     * @param gc          GraphicsContext used for drawing the canvas.
     * @param canvasWidth The width of the canvas.
     * @param game        The current state of the game.
     */
    private void drawBall(GraphicsContext gc, double canvasWidth, Game game) {
        gc.setFill(Color.YELLOWGREEN); // setting colour

        // finding ball position
        double ballX = game.getBall().getX();
        double ballY = game.getBall().getY();

        // finding ball size
        double ballSizePercentage = 0.05;
        double ballSize = canvasWidth * ballSizePercentage;

        gc.fillOval(ballX, ballY, ballSize, ballSize); // drawing ball with up-to-date values
    }

    /**
     * Draws the racket on the canvas ensuring it is the up-to-date size and position
     * It uses the player number to find which racket it needs to update.
     *
     * @param gc            GraphicsContext used for drawing the canvas.
     * @param canvasWidth   The width of the canvas.
     * @param racketWidth   The width of the racket.
     * @param racketHeight  The height of the racket.
     * @param playerNumber  The number of the player
     */
    private void drawRacket(GraphicsContext gc, double canvasWidth, double racketWidth, double racketHeight, int playerNumber) {
        gc.setFill(Color.BLUEVIOLET);

        double racketY = game.getPlayer(playerNumber).getRacket().getPosY();
        double racketX = playerNumber == 1 ? canvasWidth / 20 : canvasWidth - canvasWidth / 20 - racketWidth;

        gc.fillRect(racketX, racketY, racketWidth, racketHeight);

        game.getPlayer(playerNumber).getRacket().setPosY(racketY);
        game.getPlayer(playerNumber).getRacket().setPosX(racketX);
    }

    /**
     * Draws the racket on the canvas ensuring it is the up-to-date size and position
     * It uses the player number to find which racket it needs to update.
     *
     * @param gc            GraphicsContext used for drawing the canvas.
     * @param canvasWidth   The width of the canvas.
     * @param player        The player object
     */
    private void drawPlayerText(GraphicsContext gc, double canvasWidth, Player player) {
        double padding = 20;
        double playerTextX = player.getNumber() == 1 ? canvasWidth / 20 : canvasWidth - 200 - canvasWidth / 20;

        gc.setFill(player.getNumber() == 1 ? Color.RED : Color.GREEN);
        gc.setFont(Font.font("Verdana", FontWeight.BOLD, 20));

        gc.fillText(player.getName() + "\t\t" + player.getScore(), playerTextX, padding);
    }

    /**
     * Show goal message when a player scores a goal
     * Display the player name
     * Close the window automatically quickly so gameplay isn't disturbed.
     *
     * @param playerNumber the player number
     */
    public void showGoalMessage(int playerNumber){

        Platform.runLater(() -> {
            // Display goal message
            VBox goalMessageLayout = new VBox();
            goalMessageLayout.setAlignment(Pos.CENTER);
            goalMessageLayout.setSpacing(20);

            String goalMessage = "GOAL FOR " + game.getPlayer(playerNumber).getName();
            Label goalMessageLabel = new Label(goalMessage);
            goalMessageLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

            goalMessageLayout.getChildren().add(goalMessageLabel);

            Scene goalMessageScene = new Scene(goalMessageLayout, 400, 200);
            goalMessageScene.setFill(Color.BLACK);

            Stage goalMessageStage = new Stage();
            goalMessageStage.setTitle("GOAL");
            goalMessageStage.setScene(goalMessageScene);
            goalMessageStage.setResizable(false);
            goalMessageStage.initModality(Modality.APPLICATION_MODAL);
            goalMessageStage.show();

            // Closing VBox after a few seconds
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.3), e -> goalMessageStage.close()));
            timeline.play();
        });

    }

    /**
     * Create game over screen.
     * This displays once the game limit has been reached
     * It will show who the winning player is.
     * There is a restart button so that a new game can be played
     *
     * @param winningPlayer the winning player
     */
    public void createGameOverScreen(int winningPlayer){

    Platform.runLater(() -> {
        // Display game over message and restart button
        VBox gameOverLayout = new VBox();
        gameOverLayout.setAlignment(Pos.CENTER);
        gameOverLayout.setSpacing(20);

        String winnerMessage = game.getPlayer(winningPlayer).getName() + " Won!";
        Label gameOverLabel = new Label(winnerMessage);
        gameOverLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        Button restartButton = new Button("Restart Game");
        restartButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16px;");
        restartButton.setOnAction(e -> {
            Stage gameOverStage = (Stage) restartButton.getScene().getWindow();
            gameOverStage.close();

            game.resetGame();
        });


        gameOverLayout.getChildren().addAll(gameOverLabel, restartButton);

        Scene gameOverScene = new Scene(gameOverLayout, 400, 200);
        gameOverScene.setFill(Color.BLACK);

        Stage gameOverStage = new Stage();
        gameOverStage.setTitle("GAME OVER");
        gameOverStage.setScene(gameOverScene);
        gameOverStage.setResizable(false);
        gameOverStage.initModality(Modality.APPLICATION_MODAL);
        gameOverStage.show();
    });

}


    /**
     * Sets ball speed x.
     *
     * @param ballSpeedX the ball speed x
     */
    public void setBallSpeedX(double ballSpeedX) {
        this.ballSpeedX = ballSpeedX;
    }


    /**
     * Sets ball speed y.
     *
     * @param ballSpeedY the ball speed y
     */
    public void setBallSpeedY(double ballSpeedY) {
        this.ballSpeedY = ballSpeedY;
    }

}
