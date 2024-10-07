package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The type Database connection.
 */
public class DatabaseConnection {

    private static Connection connection;

    /**
     * This checks if this connection exists and is active.
     * If so it retrieves it
     * Otherwise it creates it.
     * This is achieved using the singleton pattern
     *
     * @return the connection
     * @throws SQLException           the sql exception
     * @throws ClassNotFoundException the class not found exception
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException
    {
        if (connection==null || connection.isClosed())
        {
            createConnection();
        }
        return connection;
    }

    /**
     * Creates a connection with the database by using the url, username and password
     *
     * @throws SQLException           the sql exception
     * @throws ClassNotFoundException the class not found exception
     */
    private static void createConnection() throws SQLException, ClassNotFoundException
    {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url="jdbc:mysql://localhost:3306/assignment";
        String username="root";
        String password="admin";
        connection=DriverManager.getConnection(url,username, password);
    }


}
