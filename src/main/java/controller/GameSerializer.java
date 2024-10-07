package controller;

import model.Game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The type Game serializer.
 */
public class GameSerializer {
    private static GameSerializer instance;

    private GameSerializer() {
    }

    /**
     * Gets instance.
     *
     * @return the instance
     */
    public static GameSerializer getInstance() {
        if (instance == null) {
            instance = new GameSerializer();
        }
        return instance;
    }


    /**
     * Serialize.
     * Saving current game state to txt file
     * Default txt file set
     * User can change the filename if they wish
     * Once completed a pop-up will show for the user to confirm whether it was successful or not
     *
     * @param game     the game
     * @param filename the filename
     */
    public void serialize(Game game, String filename) {
        try {
            FileWriter writer = new FileWriter(filename);
            // Writing save data to file
            writer.write("Player 1 Name:" + game.getPlayer(1).getName() + "\n");
            writer.write("Player 1 Score:" + game.getPlayer(1).getScore() + "\n");
            writer.write("Player 1 Racket PosX:" + game.getPlayer(1).getRacket().getPosX() + "\n");
            writer.write("Player 1 Racket PosY:" + game.getPlayer(1).getRacket().getPosY() + "\n");
            writer.write("Player 1 Racket Height:" + game.getPlayer(1).getRacket().getHeight() + "\n");
            writer.write("Player 1 Racket Width:" + game.getPlayer(1).getRacket().getWidth()  + "\n");

            writer.write("Player 2 Name:" + game.getPlayer(2).getName() + "\n");
            writer.write("Player 2 Score:" + game.getPlayer(2).getScore() + "\n");
            writer.write("Player 2 Racket PosX:" + game.getPlayer(2).getRacket().getPosX() + "\n");
            writer.write("Player 2 Racket PosY:" + game.getPlayer(2).getRacket().getPosY() + "\n");
            writer.write("Player 2 Racket Height:" + game.getPlayer(2).getRacket().getHeight() + "\n");
            writer.write("Player 2 Racket Width:" + game.getPlayer(2).getRacket().getWidth()  + "\n");

            writer.write("Score Limit:" + game.getLimit() + "\n");
            writer.write("Ball Speed X:" + game.getBall().getSpeedX() + "\n");
            writer.write("Ball Speed Y:" + game.getBall().getSpeedY() + "\n");
            writer.write("Ball Speed Timer:" + game.getBall().getTimer() + "\n");
            writer.write("Ball Speed Bounces:" + game.getBall().getMaxBounces() + "\n");

            writer.write("Ball Position X:" + game.getBall().getX() + "\n");
            writer.write("Ball Position Y:" + game.getBall().getY() + "\n");

            writer.close();

            System.out.println("Game saved!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deserialize game.
     * Loading current game state from txt file
     * Default txt file set
     * User can change the filename if they wish to load a different txt file
     * Once completed a pop-up will show for the user to confirm whether it was successful or not
     *
     * @param filename the filename
     * @param game     the game
     * @return the game
     */
    public Game deserialize(String filename, Game game) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                // stripping info from lines
                String[] parts = line.split(":");
                String key = parts[0];
                String value = parts[1];
                // updating game
                switch (key) {
                    case "Player 1 Name" -> game.getPlayer(1).setName(value);
                    case "Player 1 Score" -> game.getPlayer(1).setScore(Integer.parseInt(value));
                    case "Player 1 Racket PosX" -> game.getPlayer(1).getRacket().setPosX(Double.parseDouble(value));
                    case "Player 1 Racket PosY" -> game.getPlayer(1).getRacket().setPosY(Double.parseDouble(value));
                    case "Player 1 Racket Height" -> game.getPlayer(1).getRacket().setHeight(Double.parseDouble(value));
                    case "Player 1 Racket Width" -> game.getPlayer(1).getRacket().setWidth(Double.parseDouble(value));
                    case "Player 2 Name" -> game.getPlayer(2).setName(value);
                    case "Player 2 Score" -> game.getPlayer(2).setScore(Integer.parseInt(value));
                    case "Player 2 Racket PosX" -> game.getPlayer(2).getRacket().setPosX(Double.parseDouble(value));
                    case "Player 2 Racket PosY" -> game.getPlayer(2).getRacket().setPosY(Double.parseDouble(value));
                    case "Player 2 Racket Height" -> game.getPlayer(2).getRacket().setHeight(Double.parseDouble(value));
                    case "Player 2 Racket Width" -> game.getPlayer(2).getRacket().setWidth(Double.parseDouble(value));
                    case "Score Limit" -> game.setLimit(Integer.parseInt(value));
                    case "Ball Speed X" -> game.getBall().setSpeedX(Double.parseDouble(value));
                    case "Ball Speed Y" -> game.getBall().setSpeedY(Double.parseDouble(value));
                    case "Ball Speed Timer" -> game.getBall().setTimer(Integer.parseInt(value));
                    case "Ball Speed Bounces" -> game.getBall().setBounces(Integer.parseInt(value));
                    case "Ball Position X" -> game.getBall().setX(Double.parseDouble(value));
                    case "Ball Position Y" -> game.getBall().setY(Double.parseDouble(value));
                }

            }
            reader.close();
            System.out.println("Game loaded!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return game;
    }
}
