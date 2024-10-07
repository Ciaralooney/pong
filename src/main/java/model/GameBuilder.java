package model;

/**
 * The type Game builder.
 */
public class GameBuilder {

    private String player1Name;
    private String player2Name;
    private int player1Score;
    private int player2Score;
    private String name;
    private int target;
    private Game game;

    /**
     * Instantiates a new Game builder.
     *
     * @param game the game
     */
    public GameBuilder(Game game) {
        this.game = game;
    }

    /**
     * Building the game.
     * This is done by setting the values back to previously saved ones in the database
     *
     * @return the game
     */
    public Game build() {
        game.getPlayer(1).setName(player1Name);
        game.getPlayer(2).setName(player2Name);
        game.getPlayer(1).setScore(player1Score);
        game.getPlayer(2).setScore(player2Score);
        game.setLimit(target);

        // setting ball to default position
        game.getBall().setX(game.getGameCanvas().getWidth() / 2);
        game.getBall().setY(game.getGameCanvas().getHeight() / 2);


        return game;
    }

    /**
     * With name game builder.
     *
     * @param name the name
     * @return the game builder
     */
    public GameBuilder withName(String name) {
        this.name=name;
        return this;
    }

    /**
     * With player 1 name game builder.
     *
     * @param p1Name the p 1 name
     * @return the game builder
     */
    public GameBuilder withPlayer1Name(String p1Name) {
        this.player1Name=p1Name;
        return this;
    }

    /**
     * With player 2 name game builder.
     *
     * @param p2Name the p 2 name
     * @return the game builder
     */
    public GameBuilder withPlayer2Name(String p2Name) {
        this.player2Name=p2Name;
        return this;
    }

    /**
     * With player 1 score game builder.
     *
     * @param score the score
     * @return the game builder
     */
    public GameBuilder withPlayer1Score(int score) {
        this.player1Score=score;
        return this;
    }

    /**
     * With player 2 score game builder.
     *
     * @param score the score
     * @return the game builder
     */
    public GameBuilder withPlayer2Score(int score) {
        this.player2Score=score;
        return this;
    }

    /**
     * With target game builder.
     *
     * @param t the t
     * @return the game builder
     */
    public GameBuilder withTarget(int t) {
        this.target=t;
        return this;
    }


}
