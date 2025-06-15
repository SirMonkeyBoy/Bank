package me.sirmonkeyboy.bank.Utils;

import org.bukkit.entity.Player;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;
import java.util.OptionalDouble;
import java.util.UUID;

public class MariaDB {

    private final ConfigManager configManager;

    private HikariDataSource dataSource;

    private final String[] topPlayers = new String[10];
    private final double[] topBalances = new double[10];


    public MariaDB(ConfigManager configManager) {
        this.configManager = configManager;
    }

    // Creates the connections to the database
    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + configManager.getHost() + ":" + configManager.getPort() + "/" + configManager.getDatabase());
        config.setUsername(configManager.getUsername());
        config.setPassword(configManager.getPassword());
        config.setMaximumPoolSize(configManager.getSetMaximumPoolSize());
        config.setMinimumIdle(configManager.getSetMinimumIdle());
        config.setIdleTimeout(600000);
        config.setConnectionTimeout(30000);
        config.setLeakDetectionThreshold(10000);
        config.setMaxLifetime(1800000);
        config.setKeepaliveTime(30000);

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

    //Gets database connection
    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    // Creates the database tables if they don't exist
    @SuppressWarnings("TextBlockMigration")
    public void createTables() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement pstmt1 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS bank_balance (" +
                     "username VARCHAR(255), UUID VARCHAR(100), balance DOUBLE, PRIMARY KEY (UUID))");
             PreparedStatement pstmt2 = conn.prepareStatement("CREATE TABLE IF NOT EXISTS transactions (" +
                     "transaction_id BIGINT NOT NULL AUTO_INCREMENT UNIQUE, " +
                     "username VARCHAR(255), UUID VARCHAR(100), time TIMESTAMP, type VARCHAR(255), " +
                     "amount DOUBLE, new_balance DOUBLE, PRIMARY KEY (id), " +
                     "FOREIGN KEY (UUID) REFERENCES bank_balance(UUID) ON DELETE CASCADE)");
             PreparedStatement pstmt3 = conn.prepareStatement("CREATE INDEX IF NOT EXISTS transactions_index_0 ON transactions (UUID)")) {

            pstmt1.executeUpdate();
            pstmt2.executeUpdate();
            pstmt3.executeUpdate();
        }
    }

    // creates players row in bankbalance table
    public void createPlayer(Player player) throws SQLException {
        double balance = 0;
        UUID uuid = player.getUniqueId();
        String name = player.getName();

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement pstmt = conn.prepareStatement("SELECT username FROM bank_balance WHERE UUID = ?")) {
                pstmt.setString(1, uuid.toString());

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        String storedName = rs.getString("username");
                        if (!storedName.equalsIgnoreCase(name)) {
                            try (PreparedStatement updatePNameBank = conn.prepareStatement("UPDATE bank_balance SET username = ? WHERE UUID = ?")) {
                                updatePNameBank.setString(1, name);
                                updatePNameBank.setString(2, uuid.toString());
                                updatePNameBank.executeUpdate();
                            }

                            try (PreparedStatement updatePNameTransactions = conn.prepareStatement("UPDATE transactions SET username = ? WHERE UUID = ?")) {
                                updatePNameTransactions.setString(1, name);
                                updatePNameTransactions.setString(2, uuid.toString());
                                updatePNameTransactions.executeUpdate();
                            }
                        }
                    } else {
                        try (PreparedStatement createPlayerRow = conn.prepareStatement("INSERT IGNORE INTO bank_balance (username, UUID, balance) VALUES (?, ?, ?)")) {
                            createPlayerRow.setString(1, player.getName());
                            createPlayerRow.setString(2, player.getUniqueId().toString());
                            createPlayerRow.setDouble(3, balance);
                            createPlayerRow.executeUpdate();
                        }
                    }
                }
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                Utils.getErrorLogger("Error creating player row in database or updating the name: " + e.getMessage());
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // Gets balance from bankbalance table
    public double getBalance(UUID uuid) throws SQLException {

        double money = 0;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT BALANCE FROM bank_balance WHERE UUID=?")) {

            pstmt.setString(1, uuid.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    money = rs.getDouble("balance");
                    return money;
                }
            }
        }
        return money;
    }

    public OptionalDouble getBalanceOther(String name) throws SQLException {

        double bal;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT balance FROM bank_balance WHERE NAME=?")) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    bal = rs.getDouble("balance");
                    return OptionalDouble.of(bal);
                }
            }
        }
        return OptionalDouble.empty();
    }

    // Deposits into bank balance and tracks transaction
    public boolean depositTransaction(UUID uuid, String name, double amount) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Deposits into the players account
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "UPDATE bank_balance SET balance = balance + ? WHERE UUID = ?")) {
                    pstmt.setDouble(1, amount);
                    pstmt.setString(2, uuid.toString());

                    pstmt.executeUpdate();
                }

                // Gets the new balance
                double newBalance;
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT balance FROM bank_balance WHERE UUID = ?")) {
                    pstmt.setString(1, uuid.toString());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            newBalance = rs.getDouble("balance");
                        } else {
                            throw new SQLException("UUID not found in bank_balance");
                        }
                    }
                }

                // Adds transaction into transaction table
                try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO transactions (username, UUID, time, type, amount, new_balance) VALUES (?, ?, ?, ?, ?, ?)")) {
                    String type = "DEPOSIT";
                    long currentTimeMillis = System.currentTimeMillis();
                    java.sql.Timestamp timestamp = new java.sql.Timestamp(currentTimeMillis);
                    pstmt.setString(1, name);
                    pstmt.setString(2, uuid.toString());
                    pstmt.setTimestamp(3, timestamp);
                    pstmt.setString(4, type);
                    pstmt.setDouble(5, amount);
                    pstmt.setDouble(6, newBalance);

                    pstmt.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                Utils.getErrorLogger("Error depositing into " + name + " bank balance: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // Withdraws from bank balance and tracks transaction
    public boolean withdrawTransaction(UUID uuid, String name, double amount) throws SQLException {
        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                // Check current balance
                double currentBalance;
                try (PreparedStatement pstmt = conn.prepareStatement(
                        "SELECT balance FROM bank_balance WHERE UUID = ?")) {
                    pstmt.setString(1, uuid.toString());
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            currentBalance = rs.getDouble("balance");
                        } else {
                            throw new SQLException("UUID not found in bank_balance");
                        }
                    }
                }

                // Makes sure player has enough money in the bank
                if (currentBalance < amount) {
                    conn.rollback();
                    return false;
                }

                // Withdraws from the players account
                try (PreparedStatement pstmt = conn.prepareStatement("UPDATE bank_balance SET balance=? WHERE UUID=?")) {

                    pstmt.setDouble(1, currentBalance - amount);
                    pstmt.setString(2, uuid.toString());

                    pstmt.executeUpdate();
                }

                // Makes newBalance
                double newBalance = currentBalance - amount;

                // Adds transaction into transaction table
                try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO transactions (username, UUID, time, type, amount, new_balance) VALUES (?, ?, ?, ?, ?, ?)")) {
                    String type = "WITHDRAW";
                    long currentTimeMillis = System.currentTimeMillis();
                    java.sql.Timestamp timestamp = new java.sql.Timestamp(currentTimeMillis);
                    pstmt.setString(1, name);
                    pstmt.setString(2, uuid.toString());
                    pstmt.setTimestamp(3, timestamp);
                    pstmt.setString(4, type);
                    pstmt.setDouble(5, amount);
                    pstmt.setDouble(6, newBalance);

                    pstmt.executeUpdate();
                }

                conn.commit();
                return true;
            }catch (SQLException e) {
                conn.rollback();
                Utils.getErrorLogger("Error withdrawing from " + name + " bank balance: " + e.getMessage());
                return false;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // Gets the top 10 bank balances
    public void bankTop() throws SQLException {
        try (Connection conn = getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM bank_balance ORDER BY balance DESC LIMIT 10;");
            ResultSet rs = pstmt.executeQuery();

            int i = 0;
            while (rs.next() && i < 10) {
                topPlayers[i] = rs.getString("username");
                topBalances[i] = rs.getDouble("balance");
                i++;
            }
        }
    }

    public String[] getTopPlayers() {
        return topPlayers;
    }

    public double[] getTopBalances() {
        return topBalances;
    }
}