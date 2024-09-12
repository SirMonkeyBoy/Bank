package me.sirmonkeyboy.bank.Utill;

import me.sirmonkeyboy.bank.Bank;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class TransactionMonitor {

    private final Bank plugin;


    public TransactionMonitor(Bank plugin) {
        this.plugin = plugin;
    }

    public void createTransactionDeposit(Player p) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO transactionmonitor (ID,NAME,UUID) VALUES (?,?,?)");
            UUID uuid = p.getUniqueId();
            int id = Integer.parseInt(Objects.requireNonNull(plugin.getConfig().getString("id")));
            int one = 1;
            int newID = one + id;
            plugin.getConfig().set("id", newID);
            plugin.saveConfig();
            ps.setInt(1, newID);
            ps.setString(2, p.getName());
            ps.setString(3, uuid.toString());
            ps.executeUpdate();
            return;
        }catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    public void createTransactionWithdraw(Player p, int money) {
        try {
            PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO transactionmonitor (ID,NAME,UUID) VALUES (?,?,?)");
            UUID uuid = p.getUniqueId();
            int id = Integer.parseInt(Objects.requireNonNull(plugin.getConfig().getString("id")));
            int one = 1;
            int newID = one + id;
            plugin.getConfig().set("id", newID);
            plugin.saveConfig();
            ps.setInt(1, id);
            ps.setString(2, p.getName());
            ps.setString(3, uuid.toString());
            ps.executeUpdate();
            return;
        }catch (SQLException e) {
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

}
