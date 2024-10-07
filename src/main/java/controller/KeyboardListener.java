package controller;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.Game;
import view.GameCanvas;


/**
 * Listens for keyboard events in the game.
 * This handles keyboard input for controlling player rackets.
 */
public class KeyboardListener implements EventHandler<KeyEvent> {

    private Game game;
    private GameCanvas gameCanvas;
    private MenuListener menuListener;

    /**
     * Instantiates a new Keyboard listener.
     *
     * @param game       The current game instance.
     * @param gameCanvas The game canvas
     * @param menuListener The menu listener
     */
    public KeyboardListener(Game game, GameCanvas gameCanvas, MenuListener menuListener) {
        this.game = game;
        this.gameCanvas = gameCanvas;
        this.menuListener = menuListener;
    }

    /**
     * Handles keyboard events to control player rackets.
     * It also sets pause and unpause if enter/esc is pressed
     * It listens for certain keys to be pressed.
     * When this happens it will do the specified action
     *
     * @param keyEvent The keyboard event.
     */
    @Override
    public void handle(KeyEvent keyEvent) {
        KeyCode key = keyEvent.getCode();
        switch (key) {
            case UP -> {
                System.out.println("up");
                game.getPlayer(2).getRacket().moveUp();
            }
            case DOWN -> {
                System.out.println("down");
                game.getPlayer(2).getRacket().moveDown();
            }
            case A -> {
                System.out.println("a");
                game.getPlayer(1).getRacket().moveUp();
            }
            case Z -> {
                System.out.println("z");
                game.getPlayer(1).getRacket().moveDown();
            }
            // pausing an unpausing the game via buttons
            case ESCAPE -> {
                menuListener.unPauseGame();
            }
            case ENTER -> {
                menuListener.pauseGame();
            }

        }
        gameCanvas.redraw(game);
    }



}
