package me.sirmonkeyboy.bank;

import me.sirmonkeyboy.bank.Commands.BankCommand;
import me.sirmonkeyboy.bank.Commands.BankTop;
import me.sirmonkeyboy.bank.Listeners.PlayerJoinListener;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;
import me.sirmonkeyboy.bank.Utils.Utils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;
import java.util.Objects;

public final class Bank extends JavaPlugin {

    public MariaDB data;

    private static Economy econ = null;

    Audience console = Bukkit.getConsoleSender();

    @Override
    public void onEnable() {

        this.saveDefaultConfig();

        ConfigManager configManager = new ConfigManager(this);

        CooldownManager cooldownManager = new CooldownManager(configManager.getCooldown());

        if (!configManager.validate()) {
            getLogger().severe("Disabling due to missing config values.");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupEconomy() ) {
            getLogger().info("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        this.data = new MariaDB(this, configManager);

        try {
            data.connect();
        } catch (Exception e) {
            console.sendMessage(Component.text("[Kingdom Bank] Disabling Due to invalid Database info in config").color(NamedTextColor.DARK_RED));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        try {
            data.createTables();
        } catch (SQLException e) {
            getLogger().info("Disable Kingdom Bank due to error in Database tables");
            e.printStackTrace();
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Utils.StartBanner();

        getLogger().info("Database successfully connected");


        Objects.requireNonNull(getCommand("Bank")).setExecutor(new BankCommand(this, configManager, cooldownManager));
        Objects.requireNonNull(getCommand("BankTop")).setExecutor(new BankTop(this, configManager, cooldownManager));

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

        if (data != null && data.isConnected()) {
            data.disconnect();
            getLogger().info("Disconnected successfully from Database");
        }

        getLogger().info("Kingdom Bank has stopped");
    }
}
