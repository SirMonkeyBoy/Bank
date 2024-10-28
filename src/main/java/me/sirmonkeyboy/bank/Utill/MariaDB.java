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
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS bankbalance (NAME VARCHAR(255),UUID VARCHAR(100),BALANCE DOUBLE,PRIMARY KEY (UUID))");
            pstmt.executeUpdate();

            PreparedStatement pstmt2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS transactions (" +
                    "  id INT NOT NULL AUTO_INCREMENT UNIQUE,\n" +
                    "  uuid VARCHAR(100),\n" +
                    "  time DATETIME,\n" +
                    "  type VARCHAR(255),\n" +
                    "  amount DOUBLE,\n" +
                    "  username VARCHAR(255),\n" +
                    "  PRIMARY KEY (id),\n" +
                    "  FOREIGN KEY (uuid) REFERENCES bankbalance(uuid) ON DELETE CASCADE\n" +
                    ");");
            pstmt2.executeUpdate();
        } catch (SQLException e) {
            // Roll back the transaction if an exception occurs
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public void createPlayer(Player p) throws SQLException {
        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        try {
            UUID uuid = p.getUniqueId();
            if (!exists(uuid)) {
                PreparedStatement pstmt = conn.prepareStatement("INSERT IGNORE INTO bankbalance (NAME,UUID) VALUES (?,?)");
                pstmt.setString(1, p.getName());
                pstmt.setString(2, uuid.toString());
                pstmt.executeUpdate();

                return;
            }
        } catch (SQLException e) {
            // Roll back the transaction if an exception occurs
            conn.rollback();
            throw e;
        } finally {
            conn.close();
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

    public double getBalance(UUID uuid) throws SQLException {
        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        // Insert data into the table
        PreparedStatement pstmt = conn.prepareStatement("SELECT BALANCE FROM bankbalance WHERE UUID=?");
        pstmt.setString(1, uuid.toString());
        ResultSet rs = pstmt.executeQuery();
        double money = 0;
        if (rs.next()){
            money = rs.getDouble("BALANCE");
            return money;
        }
        // Close the connection
        conn.close();
        return money;
    }

    public void addBalance(UUID uuid, double amount) throws SQLException {
        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE bankbalance SET BALANCE=? WHERE UUID=?");
            pstmt.setDouble(1, (getBalance(uuid) + amount));
            pstmt.setString(2, uuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Roll back the transaction if an exception occurs
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public void remBalance(UUID uuid, double amount) throws SQLException {
        // Connect to the database
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        try {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE bankbalance SET BALANCE=? WHERE UUID=?");
            pstmt.setDouble(1, (getBalance(uuid) - amount));
            pstmt.setString(2, uuid.toString());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Roll back the transaction if an exception occurs
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }
}
