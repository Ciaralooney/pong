package controller;

import model.Game;
import model.GameBuilder;
import view.GameCanvas;

import java.sql.*;

/**
 * The type Database manager.
 */
public class DatabaseManager {

    private static final String save_game_query = "INSERT INTO game (player1Name, player2Name, " +
            "player1Score, player2Score, target, name) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String load_game_query = "SELECT * FROM game WHERE name = ?";

    /**
     * Save game.
     * Connecting to the database
     * Using getters with game parameter to find relevant data.
     * Using SQL to insert the data into the database
     * The data saved can be identified by the game name
     *
     * @param name the name     The name of the game save file
     * @param game the game     The game model
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException           the sql exception
     */
    public void saveGame(String name, Game game) throws ClassNotFoundException, SQLException {
        Connection connection = DatabaseConnection.getConnection();
        PreparedStatement statement = connection.prepareStatement(save_game_query);

        statement.setString(1, game.getPlayer(1).getName());
        statement.setString(2, game.getPlayer(2).getName());
        statement.setInt(3, game.getPlayer(1).getScore());
        statement.setInt(4, game.getPlayer(2).getScore());
        statement.setInt(5, game.getLimit());
        statement.setString(6, name);

        statement.executeUpdate();
        System.out.println("Your savefile is called: " + name);

    }

    /**
     * Load the latest game.
     * Query the database for the relevant data using SQL.
     * Sending it to the GameBuilder class to build the game with the save data
     *
     * @param name the name
     * @param game the game
     * @return the game
     * @throws ClassNotFoundException the class not found exception
     * @throws SQLException           the sql exception
     */
    public Game loadLatestGame(String name, Game game) throws ClassNotFoundException, SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(load_game_query)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String player1Name = resultSet.getString("player1Name");
                String player2Name = resultSet.getString("player2Name");
                int player1Score = resultSet.getInt("player1Score");
                int player2Score = resultSet.getInt("player2Score");
                int target = resultSet.getInt("target");

                return new GameBuilder(game)
                        .withName(name)
                        .withPlayer1Name(player1Name)
                        .withPlayer2Name(player2Name)
                        .withPlayer1Score(player1Score)
                        .withPlayer2Score(player2Score)
                        .withTarget(target)
                        .build();
            } else {
                return null; // game doesn't exist in database
            }
        }
    }
}