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

    public void createPlayer(Player p) throws SQLException {
        // Connect to the database

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);) {
            UUID uuid = p.getUniqueId();
            if (!exists(uuid)) {
                PreparedStatement pstmt = conn.prepareStatement("INSERT IGNORE INTO bankbalance (NAME,UUID) VALUES (?,?)");
                pstmt.setString(1, p.getName());
                pstmt.setString(2, uuid.toString());
                pstmt.executeUpdate();

                return;
            }
        }
    }

    public boolean exists(UUID uuid) throws SQLException {
        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM bankbalance WHERE UUID=?");
            pstmt.setString(1, uuid.toString());
            ResultSet results = pstmt.executeQuery();
            if (results.next()) {
                // player found
                return true;
            }
            return false;
        }catch (SQLException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        conn.close();
        return false;
    }

    public double getbalance(UUID uuid) throws SQLException {
        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        // Insert data into the table
        PreparedStatement pstmt = conn.prepareStatement("SELECT BALANCE FROM bankbalance WHERE UUID=?");
        pstmt.setString(1, uuid.toString());
        ResultSet rs = pstmt.executeQuery();
        //noinspection UnusedAssignment
        double money = 0;
        if (rs.next()){
            money = rs.getDouble("BALANCE");
            return money;
        }
        // Close the connection
        conn.close();
        return money;
    }
    
    public void addbalance(UUID uuid, double money) throws SQLException {
        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE bankbalance SET BALANCE=? WHERE UUID=?");
            pstmt.setDouble(1, (getbalance(uuid) + money));
            pstmt.setString(2, uuid.toString());
            pstmt.executeUpdate();
        }catch (SQLException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        // Close the connection
        conn.close();
    }
}
