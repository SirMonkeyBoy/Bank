package me.SirMonkeyBoy.Bank.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.SirMonkeyBoy.Bank.Main;



public class MySQL {
	
	private Main bank;

    public MySQL(Main bank) {
         this.bank = bank;
    }
	
	private String host = bank.getConfig().getString("mysql.host");
	private String port = bank.getConfig().getString("mysql.port");
	private String database = bank.getConfig().getString("mysql.database");
	private String username = bank.getConfig().getString("mysql.username");
	private String password = bank.getConfig().getString("mysql.password");
	
	private Connection connection;
	
	public boolean isConnected() {
		return (connection == null ? false : true);
	}
	
	public void connect() throws ClassNotFoundException, SQLException {
		connection = DriverManager.getConnection("jdbc:mysql://" +
			     host + ":" + port + "/" + database + "?useSSL=false",
			     username, password);
	}
}
