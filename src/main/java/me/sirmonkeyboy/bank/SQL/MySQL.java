package me.sirmonkeyboy.bank.SQL;

import me.sirmonkeyboy.bank.Bank;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private Bank plugin;

    public MySQL(Bank plugin) {
        this.plugin = plugin;
        host = plugin.getConfig().getString("host");
        port = plugin.getConfig().getString("port");
        database = plugin.getConfig().getString("database");
        username = plugin.getConfig().getString("username");
        password = plugin.getConfig().getString("password");
    }

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    private Connection connection;

    public boolean isConnected() {
        return (connection == null ? false : true);
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
