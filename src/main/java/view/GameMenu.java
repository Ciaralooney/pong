package view;

import controller.MenuListener;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import model.Game;

import java.sql.SQLException;

/**
 * The type My menu.
 */
public class GameMenu{
    private MenuItem menuItemSelectPlayer1Name;
    private MenuItem menuItemSelectPlayer2Name;
    private MenuBar menuBar;
    private Menu menuFile;
    private Menu menuHelp;
    private MenuItem menuItemExit;
    private MenuItem menuItemAbout;
    private MenuItem menuItemGameLimit;
    private MenuListener menuListener;

    private MenuItem menuItemSaveGame;
    private MenuItem menuItemLoadGame;
    private MenuItem menuItemSaveGameDB;
    private MenuItem menuItemLoadGameDB;

    /**
     * Instantiates a new Game menu.
     * This game menu controls settings and options.
     *   It allows the user to adjust game limits, setting ball speed,
     *   racket dimensions, selecting player names, pausing, resuming, exiting etc.
     *
     * @param menuListener the menu listener
     */
    public GameMenu(MenuListener menuListener) {
        this.menuListener = menuListener;
        menuBar = new MenuBar();
        menuFile = new Menu("File");
        menuHelp = new Menu("Help");
        menuItemExit = new MenuItem("Exit");
        menuItemAbout = new MenuItem("About");
        menuItemGameLimit = new MenuItem("Game Limit");

        menuItemSelectPlayer1Name = new MenuItem("Select P1 Name");
        menuItemSelectPlayer2Name = new MenuItem("Select P2 Name");

        MenuItem menuItemBallSpeed = new MenuItem("Set Ball Speed");

        MenuItem menuItemRacketSize = new MenuItem("Adjust Racket Dimensions");

        menuItemRacketSize.setOnAction(e -> menuListener.adjustRacketSize());

        MenuItem menuItemSpeedFrequency = new MenuItem("Ball Speed Timer");
        menuItemSpeedFrequency.setOnAction(e -> menuListener.setBallSpeedFrequency());

        menuItemSaveGame = new MenuItem("Save Game");
        menuItemSaveGame.setOnAction(e -> menuListener.saveGame());
        menuItemLoadGame = new MenuItem("Load Game");
        menuItemLoadGame.setOnAction(e -> menuListener.loadGame());

        menuItemSaveGameDB = new MenuItem("Save Game to DB");
        menuItemSaveGameDB.setOnAction(e -> menuListener.saveGameToDB(menuListener.getGame()));

        menuItemLoadGameDB = new MenuItem("Load Game from DB");
        menuItemLoadGameDB.setOnAction(e -> {
            try {
                menuListener.loadGameFromDB();
            } catch (SQLException | ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });

        menuFile.getItems().addAll(menuItemSaveGame, menuItemLoadGame, menuItemExit, menuItemGameLimit);
        menuHelp.getItems().add(menuItemAbout);
        menuBar.getMenus().addAll(menuFile, menuHelp);
        menuItemBallSpeed.setOnAction(e -> menuListener.setBallSpeed());

        handleClicking(menuListener);
    }

    // this handles the menu items when they are chosen.
    private void handleClicking(MenuListener menuListener) {
        menuItemExit.setOnAction(e->menuListener.setExit());
        menuItemAbout.setOnAction(e->menuListener.setAbout());
        menuItemGameLimit.setOnAction(e->{menuListener.setGameLimit();});
        menuItemSelectPlayer1Name.setOnAction(e -> menuListener.selectPlayerName(1));
        menuItemSelectPlayer2Name.setOnAction(e -> menuListener.selectPlayerName(2));

    }
}