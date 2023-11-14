package me.sirmonkeyboy.bank.SQL;

import me.sirmonkeyboy.bank.Bank;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SQLGetter {

    private final Bank plugin;

    public SQLGetter(Bank plugin){
        this.plugin = plugin;
    }

    public void createTable(){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS bankbalance (NAME VARCHAR(100),UUID VARCHAR(100),BALANCE INT(100),PRIMARY KEY (NAME))");
            ps.executeUpdate();
        }catch (SQLException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void createPlayer(Player p) {
        try {
            UUID uuid = p.getUniqueId();
            if (!exists(uuid)) {
                PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO bankbalance (NAME,UUID) VALUES (?,?)");
                ps.setString(1, p.getName());
                ps.setString(2, uuid.toString());
                ps.executeUpdate();

                return;
            }
        }catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public boolean exists(UUID uuid) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM bankbalance WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                // player found
                return true;
            }
            return false;
        }catch (SQLException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return false;
    }

    public int getbalance(UUID uuid) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT BALANCE FROM bankbalance WHERE UUID=?");
            ps.setString(1, uuid.toString());
            ResultSet rs =ps.executeQuery();
            //noinspection UnusedAssignment
            int money = 0;
            if (rs.next()){
                money = rs.getInt("BALANCE");
                return money;
            }
        }catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
        return 0;
    }

    public void addbalance(UUID uuid, int money){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE bankbalance SET BALANCE=? WHERE UUID=?");
            ps.setInt(1, (getbalance(uuid) + money));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void rembalance(UUID uuid, int money){
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE bankbalance SET BALANCE=? WHERE UUID=?");
            ps.setInt(1, (getbalance(uuid) - money));
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        }catch (SQLException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void createTableTransactionMonitor(){
        PreparedStatement ps;
        try {
            ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS transactionmonitor (ID VARCHAR(100),NAME VARCHAR(100),UUID VARCHAR(100),AMOUNTADDEDTAKEN VARCHAR(100),PRIMARY KEY (ID))");
            ps.executeUpdate();
        }catch (SQLException e){
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

}
