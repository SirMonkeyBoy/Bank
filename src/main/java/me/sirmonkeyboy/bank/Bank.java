package me.sirmonkeyboy.bank;

import me.sirmonkeyboy.bank.Commands.ABankCommand;
import me.sirmonkeyboy.bank.Commands.BankCommand;
import me.sirmonkeyboy.bank.Commands.BankTop;
import me.sirmonkeyboy.bank.Listeners.PlayerJoinListener;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;
import me.sirmonkeyboy.bank.Utils.Utils;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;
import java.util.Objects;

public final class Bank extends JavaPlugin {

    public MariaDB data;

    private static Economy econ = null;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        ConfigManager configManager = new ConfigManager(this);
        CooldownManager cooldownManager = new CooldownManager(configManager.getCooldown());
        MariaDB data = new MariaDB(this, configManager);

        /* Checks to make sure on startup that all config variables are there
         if not plugin will shut down. */
        if (!configManager.validate()) {
            Utils.getErrorLogger("Disabling due to missing config values.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        /* Checks to make sure that Vault and an Economy plugin is installed
         if not plugin will shut down. */
        if (!setupEconomy() ) {
            Utils.getErrorLogger("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* Attempts to connect to the database if fails will plugin shut down */
        try {
            data.connect();
        } catch (Exception e) {
            Utils.getErrorLogger("Disabling Due to invalid Database info in config");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* Attempts to make database tables if fails will plugin shut down */
        try {
            data.createTables();
        } catch (SQLException e) {
            Utils.getErrorLogger("Disable Kingdom Bank due to error in Database tables");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        getLogger().info("Database successfully connected");

        /* Registers commands */
        Objects.requireNonNull(getCommand("Bank")).setExecutor(new BankCommand(this, configManager, cooldownManager));
        Objects.requireNonNull(getCommand("BankTop")).setExecutor(new BankTop(this, configManager, cooldownManager));
        Objects.requireNonNull(getCommand("ABank")).setExecutor(new ABankCommand(this, configManager, cooldownManager));

        /* Registers join listener */
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this),this);

        /* Start messages */
        Utils.getStartBanner();
        getLogger().info("Bank has started");
    }

    /* Check for Vault and an Economy plugin logic*/
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

        /* If database connected disconnects from database */
        if (data != null && data.isConnected()) {
            data.disconnect();
            getLogger().info("Disconnected successfully from Database");
        }

        getLogger().info("Kingdom Bank has stopped");
    }
}
