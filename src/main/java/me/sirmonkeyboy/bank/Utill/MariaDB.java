package me.sirmonkeyboy.bank.Utill;

import me.sirmonkeyboy.bank.Bank;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import org.bukkit.entity.Player;

import java.sql.*;
import java.util.UUID;

public class MariaDB {

    private final Bank plugin;

    private HikariDataSource dataSource;

    private final String host;
    private final String port;
    private final String database;
    private final String username;
    private final String password;

    public MariaDB(Bank plugin) {
        this.plugin = plugin;

        host = plugin.getConfig().getString("mariaDB.host");
        port = plugin.getConfig().getString("mariaDB.port");
        database = plugin.getConfig().getString("mariaDB.database");
        username = plugin.getConfig().getString("mariaDB.username");
        password = plugin.getConfig().getString("mariaDB.password");
    }

    // Creates the connections to the database
    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(2);
        config.setIdleTimeout(30000);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(10000);

        dataSource = new HikariDataSource(config);
    }

    // Checks if the database is connected
    public boolean isConnected() {
        return (dataSource != null && !dataSource.isClosed());
    }

    // Disconnects for the database if connected
    public void disconnect() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
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
                    "  username VARCHAR(255),\n" +
                    "  uuid VARCHAR(100),\n" +
                    "  time TIMESTAMP,\n" +
                    "  type VARCHAR(255),\n" +
                    "  amount DOUBLE,\n" +
                    "  newbalance DOUBLE,\n" +
                    "  PRIMARY KEY (id),\n" +
                    "  FOREIGN KEY (uuid) REFERENCES bankbalance(uuid) ON DELETE CASCADE\n" +
                    ");");
            pstmt2.executeUpdate();

            PreparedStatement pstmt3 = conn.prepareStatement("CREATE INDEX IF NOT EXISTS transactions_index_0 ON transactions (uuid);");
            pstmt3.executeUpdate();
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

    public void DepositTransaction(UUID uuid, String name, double amount, double newbalance) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO transactions (username, uuid, time, type, amount, newbalance) VALUES (?, ?, ?, ?, ?, ?)");
            String type = "DEPOSIT";
            long currentTimeMillis = System.currentTimeMillis();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(currentTimeMillis);
            pstmt.setString(1, name);
            pstmt.setString(2, uuid.toString());
            pstmt.setTimestamp(3, timestamp);
            pstmt.setString(4, type);
            pstmt.setDouble(5, amount);
            pstmt.setDouble(6, newbalance);
            pstmt.executeUpdate();
            conn.commit();
        }catch (SQLException e) {
            // Roll back the transaction if an exception occurs
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public void WithdrawTransaction(UUID uuid, String name, double amount, double newbalance) throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        try {
            conn.setAutoCommit(false);
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO transactions (username, uuid, time, type, amount, newbalance) VALUES (?, ?, ?, ?, ?, ?)");
            String type = "WITHDRAW";
            long currentTimeMillis = System.currentTimeMillis();
            java.sql.Timestamp timestamp = new java.sql.Timestamp(currentTimeMillis);
            pstmt.setString(1, name);
            pstmt.setString(2, uuid.toString());
            pstmt.setTimestamp(3, timestamp);
            pstmt.setString(4, type);
            pstmt.setDouble(5, amount);
            pstmt.setDouble(6, newbalance);
            pstmt.executeUpdate();
            conn.commit();
        }catch (SQLException e) {
            // Roll back the transaction if an exception occurs
            conn.rollback();
            throw e;
        } finally {
            conn.close();
        }
    }

    public void BankTop() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false", username, password);

        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM bankbalance ORDER BY BALANCE DESC LIMIT 10;");
        ResultSet rs = pstmt.executeQuery();

        int i = 0;
        while (rs.next() && i < 10) {
            topPlayers[i] = rs.getString("NAME");
            topBalances[i] = rs.getDouble("BALANCE");
            i++;
        }

        conn.close();
    }

    public String[] getTopPlayers() {
        return topPlayers;
    }

    public double[] getTopBalances() {
        return topBalances;
    }
}
