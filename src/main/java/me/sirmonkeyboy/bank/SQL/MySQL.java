package me.sirmonkeyboy.bank.SQL;

import me.sirmonkeyboy.bank.Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    @SuppressWarnings("FieldCanBeLocal")
    private final Bank plugin;

    public MySQL(Bank plugin) {
        this.plugin = plugin;
        host = plugin.getConfig().getString("mysql.host");
        port = plugin.getConfig().getString("mysql.port");
        database = plugin.getConfig().getString("mysql.database");
        username = plugin.getConfig().getString("mysql.username");
        password = plugin.getConfig().getString("mysql.password");
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
}
