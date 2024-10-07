package controller;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import model.Game;
import view.GameCanvas;
import java.util.Random;
import java.sql.SQLException;
import java.util.Optional;

/**
 * The type Menu listener.
 */
public class MenuListener {
    private GameSerializer gameSerializer;
    private GameController gameController;
    private Stage primaryStage;
    private GameCanvas gameCanvas;
    private Game game;
    Random random = new Random();
    private DatabaseConnection databaseConnection;
    // Generating a unique default game save
    private String gameName = "GameSave_" + (String.valueOf(random.nextInt(10)) +
            (String.valueOf(random.nextInt(10)) + (String.valueOf(random.nextInt(10)))));

    /**
     * Instantiates a new Menu listener.
     *
     * @param gameController The game controller managing the game.
     * @param primaryStage   The primary stage of the JavaFX application.
     * @param gameCanvas     The game canvas
     * @param game           The current game instance.
     * @throws SQLException the sql exception
     */
    public MenuListener(GameController gameController, Stage primaryStage, GameCanvas gameCanvas, Game game) throws SQLException {
        this.gameController = gameController;
        this.primaryStage = primaryStage;
        this.gameCanvas = gameCanvas;
        this.game = game;
        this.gameSerializer = GameSerializer.getInstance();
        this.databaseConnection = getDatabaseConnection();
    }

    /**
     * Gets database connection.
     *
     * @return the database connection
     */
    public DatabaseConnection getDatabaseConnection() {
        return databaseConnection;
    }


    /**
     * Creates the interactive menu bar for the game interface.
     *
     * @return The created menu bar.
     */
    public MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // file manu
        MenuItem gameLimitMenuItem = new MenuItem("Score Limit");
        gameLimitMenuItem.setOnAction(e -> setGameLimit());

        MenuItem ballSpeedMenuItem = new MenuItem("Ball Speed");
        ballSpeedMenuItem.setOnAction(e -> setBallSpeed());

        MenuItem ballSpeedFrequencyMenuItem = new MenuItem("Ball Speed Timer");
        ballSpeedFrequencyMenuItem.setOnAction(e -> setBallSpeedFrequency());

        MenuItem ballSpeedBouncesMenuItem = new MenuItem("Ball Speed Bounces");
        ballSpeedBouncesMenuItem.setOnAction(e -> setBallSpeedBounces());

        MenuItem adjustRacketDimensionsMenuItem = new MenuItem("Adjust Racket Size");
        adjustRacketDimensionsMenuItem.setOnAction(e -> adjustRacketSize());

        MenuItem pauseMenuItem = new MenuItem("Pause");
        pauseMenuItem.setOnAction(e -> pauseGame());

        MenuItem resumeMenuItem = new MenuItem("Resume");
        resumeMenuItem.setOnAction(e -> unPauseGame());

        MenuItem restartGameItem = new MenuItem("Restart");
        restartGameItem.setOnAction(e -> game.resetGame());

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> setExit());

        // player menu
        Menu playersMenu = new Menu("Players");

        MenuItem chooseP1NameMenuItem = new MenuItem("Choose P1 Name");
        chooseP1NameMenuItem.setOnAction(e -> selectPlayerName(1));

        MenuItem chooseP2NameMenuItem = new MenuItem("Choose P2 Name");
        chooseP2NameMenuItem.setOnAction(e -> selectPlayerName(2));

        playersMenu.getItems().addAll(chooseP1NameMenuItem, chooseP2NameMenuItem);

        // help menu
        MenuItem aboutMenuItem = new MenuItem("About");
        aboutMenuItem.setOnAction(e -> setAbout());

        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().addAll(aboutMenuItem);

        // save/load options serialization
        MenuItem menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setOnAction(e -> saveGame());

        MenuItem menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(e -> loadGame());

        Menu menuSaveGame = new Menu("Save Game");

        Menu menuLoadGame = new Menu("Load Game");

        // save/load options from database
        MenuItem menuItemSaveGameDB = new MenuItem("Save Game to DB");
        menuItemSaveGameDB.setOnAction(e -> saveGameToDB(game));

        MenuItem menuItemLoadGameDB = new MenuItem("Load Game from DB");
        menuItemLoadGameDB.setOnAction(e -> {
            try {
                loadGameFromDB();
            } catch (SQLException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        // add menus to menu bar
        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(gameLimitMenuItem,
                pauseMenuItem,
                resumeMenuItem,
                ballSpeedMenuItem,
                ballSpeedFrequencyMenuItem,
                ballSpeedBouncesMenuItem,
                adjustRacketDimensionsMenuItem,
                restartGameItem,
                exitMenuItem
        );

        menuSaveGame.getItems().addAll(menuItemSaveGame, menuItemSaveGameDB);
        menuLoadGame.getItems().addAll(menuItemLoadGame, menuItemLoadGameDB);

        menuBar.getMenus().addAll(fileMenu, playersMenu, helpMenu, menuSaveGame, menuLoadGame);

        return menuBar;
    }

    /**
     * Sets game limit.
     * When the user clicks on this menu option the ball is paused to give the user time to input.
     * An input box will appear prompting the user to set a game limit.
     * The game limit will mean that once a user's score reaches this limit the game ends.
     * If a number is entered then the game limit is changed to the new limit.
     */
    public void setGameLimit() {
        pauseGame();
        TextInputDialog dialog = new TextInputDialog(String.valueOf(game.getLimit()));  // Display current limit

        dialog.setTitle("Game Limit");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Max Score:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(limit -> {
            int gameLimit = Integer.parseInt(limit);
            game.setLimit(gameLimit);
            System.out.println("Game Limit: " + gameLimit);
        });
        unPauseGame();
    }

    /**
     * Pause game, rackets and ball can't move
     */
    public void pauseGame() {
        game.getBall().pause();
        game.getPlayer(1).getRacket().pause();
        game.getPlayer(2).getRacket().pause();
        System.out.println("Game Paused ☕");
    }

    /**
     * Unpause game, rackets and ball can move
     */
    public void unPauseGame() {
        game.getBall().resume();
        game.getPlayer(1).getRacket().resume();
        game.getPlayer(2).getRacket().resume();
        System.out.println("Game Unpaused");
    }

    /**
     * Save game data to database.
     * It first pauses the game
     * Takes a game name entered by the user
     * If no name is inputted then a default game name is generated.
     * It passed the current game state and gamename into the savegame function in the database manager class
     * The game is then unpaused
     *
     * @param game the game
     */
    public void saveGameToDB(Game game) {
        pauseGame();
        TextInputDialog dialog = new TextInputDialog(gameName); // default name here
        dialog.setTitle("Save Game to Database");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter game name");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(gameName -> {
            DatabaseManager databaseManager = new DatabaseManager();
            try {
                databaseManager.saveGame(gameName, game); // save game data to database
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Game saved to the database!");
            alert.showAndWait();
        });

        unPauseGame();
    }

    /**
     * Method to load game data from the database
     * It first pauses the game
     * Takes a game name entered by the user
     * If no name is inputted then a default game name is generated.
     * It passed the current game state and gamename into the loadLatestGame function in the database manager class
     * The game from the database is now playing
     * The game is then unpaused
     *
     * @throws SQLException           the sql exception
     * @throws ClassNotFoundException the class not found exception
     */
    public void loadGameFromDB() throws SQLException, ClassNotFoundException {
        pauseGame();

        DatabaseManager databaseManager = new DatabaseManager();
        TextInputDialog dialog = new TextInputDialog(gameName); // default name here
        dialog.setTitle("Load game from Database");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter game name");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(gameName -> {
            Game loadedGame;
            try {
                loadedGame = databaseManager.loadLatestGame(gameName, game); // save game data to database
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

            if (loadedGame != null) {
                gameController.setGame(loadedGame);
                gameCanvas.redraw(loadedGame);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Game loaded from the database successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Failure");
                alert.setHeaderText(null);
                alert.setContentText("No game found in the database!");
                alert.showAndWait();
            }
        });

        unPauseGame();
    }

    /**
     * Sets exit.
     * When chosen this closes the game.
     */
    public void setExit() {
        Platform.exit();
    }

    /**
     * Sets about.
     * When the user clicks on this menu option the ball is paused to give the user time to read.
     * A box will pop up with information about the game.
     */
    public void setAbout() {
        pauseGame();
        var alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Super Ping pong");
        alert.setHeaderText("Made in Cork by Ciara");
        alert.setContentText("All rights reserved");
        alert.showAndWait().ifPresent((btnType) -> {
        });
        unPauseGame();
    }

    /**
     * Save game to the default txt file
     * If the user selects a different txt file it will save to that instead
     * Calls the serialize function to save the game.
     */
    public void saveGame() {
        pauseGame();
        TextInputDialog dialog = new TextInputDialog("saved_game.txt"); // default name
        dialog.setTitle("Save Game");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter filename:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(filename -> {
            gameSerializer.serialize(game, filename);
        });

        unPauseGame();
    }

    /**
     * Load game from the default txt file
     * If the user selects a different txt file it will load from that instead
     * Calls the deserialize function to load the game.
     */
    public void loadGame() {
        pauseGame();
        TextInputDialog dialog = new TextInputDialog("saved_game.txt"); // default name
        dialog.setTitle("Load Game");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter filename:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(filename -> {
            Game loadedGame = gameSerializer.deserialize(filename, game);
            if (loadedGame != null) {
                gameController.setGame(loadedGame);
                gameCanvas.redraw(loadedGame);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Game loaded successfully!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Failure");
                alert.setHeaderText(null);
                alert.setContentText("Game save not found!");
                alert.showAndWait();
            }
        });
        unPauseGame();
    }

    /**
     * Adjust racket size.
     * When the user clicks on this menu option the ball is paused to give the user time to input.
     * An input box will appear prompting the user to set a racket width and height.
     * This will change the size of the rackets in the game which can make the game easier or harder
     * If a width/height is entered then this is set to the new racket height.
     */
    public void adjustRacketSize() {
        pauseGame();
        TextInputDialog widthDialog = new TextInputDialog();
        widthDialog.setTitle("Racket Width");
        widthDialog.setHeaderText(null);
        widthDialog.setContentText("Enter Racket Width:");

        Optional<String> widthResult = widthDialog.showAndWait();

        widthResult.ifPresent(width -> {
            double racketWidth = Double.parseDouble(width);

            TextInputDialog heightDialog = new TextInputDialog();
            heightDialog.setTitle("Racket Height");
            heightDialog.setHeaderText(null);
            heightDialog.setContentText("Enter Racket Height:");

            Optional<String> heightResult = heightDialog.showAndWait();

            heightResult.ifPresent(height -> {
                double racketHeight = Double.parseDouble(height);
                game.getPlayer(1).getRacket().setWidth(racketWidth);
                game.getPlayer(1).getRacket().setHeight(racketHeight);
                game.getPlayer(2).getRacket().setWidth(racketWidth);
                game.getPlayer(2).getRacket().setHeight(racketHeight);
                Platform.runLater(()->{
                    gameCanvas.redraw(game);
                });
            });
        });
        unPauseGame();
    }

    /**
     * Sets Ball Speed.
     * When the user clicks on this menu option the ball is paused to give the user time to input.
     * An input box will appear prompting the user to set a ball speed.
     * This will change the speed of the ball in the game which can make the game easier or harder.
     * The ball needs 2 speeds (x and y) so it can bounce around randomly and not just straight.
     * If a number is entered then this is set to the new speed x and y for the ball.
     */
    public void setBallSpeed() {
        pauseGame();
        TextInputDialog dialog = new TextInputDialog(String.valueOf(game.getBall().getSpeedX()));

        dialog.setTitle("Ball Speed X");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Ball Speed X:");

        Optional<String> resultx = dialog.showAndWait();

        TextInputDialog question = new TextInputDialog(String.valueOf(game.getBall().getSpeedY()));

        question.setTitle("Ball Speed Y");
        question.setHeaderText(null);
        question.setContentText("Enter Ball Speed Y:");

        Optional<String> resultY = question.showAndWait();

        resultx.ifPresent(speed -> {
            double ballSpeedX = Double.parseDouble(speed);
            gameCanvas.setBallSpeedX(ballSpeedX);
            Platform.runLater(()->{
                game.getBall().setSpeedX(ballSpeedX);
            });
            System.out.println("Ball Speed: " + ballSpeedX);
        });

        resultY.ifPresent(speed -> {
            double ballSpeedY = Double.parseDouble(speed);
            gameCanvas.setBallSpeedY(ballSpeedY);
            Platform.runLater(()->{
                game.getBall().setSpeedY(ballSpeedY);
            });
            System.out.println("Ball Speed: " + ballSpeedY);
            unPauseGame();
        });
    }


    /**
     * Sets Ball Speed Frequency
     * When the user clicks on this menu option the ball is paused to give the user time to input.
     * An input box will appear prompting the user to set a ball speed frequency
     * This will change the speed of the ball in a set amount of time.
     * This can make the game easier or harder.
     * The ball will increase speed every time this number counts down
     * If a number is entered then this is set to the new speed frequency for the ball
     */
    public void setBallSpeedFrequency() {
        pauseGame(); // pausing ball while menu is open
        TextInputDialog dialog = new TextInputDialog(String.valueOf(game.getBall().getTimer()));

        dialog.setTitle("Ball Speed Timer");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Ball Speed Timer:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(frequency -> {
            int timer = Integer.parseInt(frequency);
            game.getBall().setTimer(timer);
        });
        unPauseGame();
    }


    /**
     * Sets Ball Speed Bounces
     * When the user clicks on this menu option the ball is paused to give the user time to input.
     * An input box will appear prompting the user to set a ball speed bouncing counter
     * This will change the speed of the ball every time the ball bounces this many times.
     * This can make the game easier or harder.
     * If a number is entered then this is set to the new speed bouncer for the ball
     */
    public void setBallSpeedBounces() {
        pauseGame(); // pausing ball while menu is open
        TextInputDialog dialog = new TextInputDialog(String.valueOf(game.getBall().getMaxBounces()));

        dialog.setTitle("Ball Speed after bounces");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter how many bounces:");

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(frequency -> {
            int bounces = Integer.parseInt(frequency);
            game.getBall().setBounces(bounces);
        });
        unPauseGame();
    }

    /**
     * Select player name.
     * When the user clicks on this menu option the ball is paused to give the user time to input.
     * An input box will appear prompting the user to enter a name for their chosen player.
     * This will change the name for this player and redraw it on the canvas
     * If a string is entered then this is set to the new player name
     *
     * @param playerNumber the player number
     */
    public void selectPlayerName(Integer playerNumber) {
        pauseGame();
        TextInputDialog dialog = new TextInputDialog(game.getPlayer(playerNumber).getName());
        dialog.setTitle("Select Player Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter Player Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            game.getPlayer(playerNumber).setName(name);
        });
        unPauseGame();
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
     * @param game the game
     */
    public void setGame(Game game) {
        this.game = game;
    }


}
