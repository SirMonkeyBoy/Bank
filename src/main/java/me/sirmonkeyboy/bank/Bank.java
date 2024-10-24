package me.sirmonkeyboy.bank;

import me.sirmonkeyboy.bank.Commands.CommandManager;
import me.sirmonkeyboy.bank.Listeners.PlayerJoinListener;

import me.sirmonkeyboy.bank.SQL.MySQL;
import me.sirmonkeyboy.bank.Utill.MariaDB;
import me.sirmonkeyboy.bank.Utill.TransactionMonitor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;
import java.util.Objects;

public final class Bank extends JavaPlugin {
    public MySQL SQL;
    public MariaDB data;

    private static Economy econ = null;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        if (!setupEconomy() ) {
            getLogger().info("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        this.SQL =new MySQL(this);
        this.data = new MariaDB(this);

        try {
            data.connect();
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().info("Database not connected");
            getLogger().info("Disabled due to no Database found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Database is connected");
        try {
            data.createTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Objects.requireNonNull(getCommand("Bank")).setExecutor(new CommandManager(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this),this);

        getLogger().info("Kingdom Bank has started");
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }

        econ = rsp.getProvider();
        //noinspection ConstantValue
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        data.disconnect();
        getLogger().info("Disconnected from Database");
        getLogger().info("kingdom Bank has stopped");
    }
}
