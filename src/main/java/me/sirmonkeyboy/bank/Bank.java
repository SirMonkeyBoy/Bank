package me.sirmonkeyboy.bank;

import me.sirmonkeyboy.bank.Commands.CommandManager;
import me.sirmonkeyboy.bank.Listeners.PlayerJoinListener;
import me.sirmonkeyboy.bank.SQL.MySQL;
import me.sirmonkeyboy.bank.SQL.SQLGetter;

import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;
import java.util.Objects;

public final class Bank extends JavaPlugin {

    public MySQL SQL;
    public SQLGetter data;

    private static Economy econ = null;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        if (!setupEconomy() ) {
            getLogger().info("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.SQL = new MySQL(this);
        this.data = new SQLGetter(this);

        try {
            SQL.connect();
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().info("Database not connected");
        }

        if (SQL.isConnected()){
            getLogger().info("Database is connected");
            data.createTable();
        }

        Objects.requireNonNull(getCommand("Bank")).setExecutor(new CommandManager(this));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this),this);

        getLogger().info("Bank has started");
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
        SQL.disconnect();

        getLogger().info("Bank has stopped");
    }
}
