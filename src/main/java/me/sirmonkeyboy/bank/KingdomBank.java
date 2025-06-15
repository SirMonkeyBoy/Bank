package me.sirmonkeyboy.bank;

import me.sirmonkeyboy.bank.Commands.ABankCommand;
import me.sirmonkeyboy.bank.Commands.BankCommand;
import me.sirmonkeyboy.bank.Commands.BankTop;
import me.sirmonkeyboy.bank.Listeners.PlayerJoinListener;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;
import me.sirmonkeyboy.bank.Utils.Utils;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import java.util.Objects;

public final class KingdomBank extends JavaPlugin {

    private  MariaDB data;

    private static Economy econ = null;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        ConfigManager configManager = new ConfigManager(this);
        CooldownManager cooldownManager = new CooldownManager(configManager.getCooldown());
        this.data = new MariaDB(this, configManager);

        /* Checks to make sure on startup that all config variables are there
         if not plugin will shut down. */
        if (!configManager.validate()) {
            Utils.getErrorLogger("Disabling Kingdom Bank due to missing config values.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        /* Checks to make sure that Vault and an Economy plugin is installed
         if not plugin will shut down. */
        if (!setupEconomy() ) {
            Utils.getErrorLogger("Disabling Kingdom Bank due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* Attempts to connect to the database if fails will plugin shut down */
        try {
            data.connect();
        } catch (Exception e) {
            Utils.getErrorLogger("Disabling Kingdom Bank due to invalid Database config: " + e.getMessage());
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        /* Attempts to make database tables if fails will plugin shut down */
        data.createTables((success) -> {
            if (!success) {
                Utils.getErrorLogger("Disabling Kingdom Bank due to error creating Database tables.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }

            Utils.logger(Component.text("Database connected successfully"));

            // Register commands
            Objects.requireNonNull(getCommand("Bank")).setExecutor(new BankCommand(data, configManager, cooldownManager));
            Objects.requireNonNull(getCommand("BankTop")).setExecutor(new BankTop(data, configManager, cooldownManager));
            Objects.requireNonNull(getCommand("ABank")).setExecutor(new ABankCommand(this, data, configManager, cooldownManager));

            // Register listeners
            getServer().getPluginManager().registerEvents(new PlayerJoinListener(data), this);

            // Banner and startup message
            Utils.getStartBanner();
            Utils.logger(Component.text("Kingdom Bank has started"));
        });
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

        Bukkit.getAsyncScheduler().cancelTasks(this);

        /* If database connected disconnects from database */
        if (data != null && data.isConnected()) {
            data.disconnect();
            Utils.logger(Component.text("Disconnected successfully from Database"));
        }

        Utils.logger(Component.text("Kingdom Bank has stopped"));
    }
}
