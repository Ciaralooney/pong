package com.example.myfirstgame;

import controller.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Game;
import view.GameCanvas;


/**
 * The type Main application.
 */
public class MainApplication extends Application {
    private Game game;
    private GameCanvas gameCanvas;
    private GameController gameController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        gameController = new GameController(primaryStage);
        this.gameController.setStage(primaryStage);
        game = gameController.getGame();
        gameCanvas = gameController.getGameCanvas();

        MenuListener menuListener = gameController.getMenuListener();
        MenuBar menuBar = menuListener.createMenuBar();
        KeyboardListener keyboardListener = new KeyboardListener(game, gameCanvas, menuListener);
        gameCanvas.setOnKeyPressed(keyboardListener);
        gameCanvas.setOnKeyTyped(keyboardListener);
        gameCanvas.setFocusTraversable(true);

        BorderPane root = new BorderPane(gameCanvas);
        root.setTop(menuBar);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("My Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        BallManager ballManager = gameController.getBallManager();
        Thread thread = new Thread(ballManager);
        thread.start();
        Thread.yield();

        scene.widthProperty().addListener((observable, oldValue, newValue) -> gameCanvas.setWidth(newValue.doubleValue()));
        scene.heightProperty().addListener((observable, oldValue, newValue) -> gameCanvas.setHeight(newValue.doubleValue()));

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
    }
}
