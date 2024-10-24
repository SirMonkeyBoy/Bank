package me.sirmonkeyboy.bank.Utill;

import me.sirmonkeyboy.bank.Bank;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class MariaDB {
    private final Bank plugin;

    public MariaDB(Bank plugin) {
        this.plugin = plugin;
        host = plugin.getConfig().getString("mariaDB.host");
        port = plugin.getConfig().getString("mariaDB.port");
        database = plugin.getConfig().getString("mariaDB.database");
        username = plugin.getConfig().getString("mariaDB.username");
        password = plugin.getConfig().getString("mariaDB.password");
    }

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    private Connection connection;

    public boolean isConnected() {
        return (connection != null);
    }

    public void connect() throws ClassNotFoundException, SQLException {
        if (!isConnected()) {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);
        }
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                connection.close();
            }catch (SQLException e){
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
    
    public void createTables() throws SQLException {
        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        try {
            PreparedStatement pstmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS bankbalance (NAME VARCHAR(100),UUID VARCHAR(100),BALANCE INT(100),PRIMARY KEY (NAME))");
            pstmt.executeUpdate();
        }catch (SQLException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        // Close the connection
        conn.close();
    }
}
