package me.sirmonkeyboy.bank.Utils;

import me.sirmonkeyboy.bank.KingdomBank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.ArrayList;

public class ConfigManager {

    private final KingdomBank plugin;

    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private String balanceMessage;
    private String depositMessage;
    private String dontHaveEnoughInBalanceDeposit;
    private String minimumDepositMessage;
    private String withdrawMessage;
    private String dontHaveEnoughInBalanceWithdraw;
    private String minimumWithdrawMessage;
    private String cooldownMessage;
    private String invalidAmount;
    private String noPermission;
    private String youCantRunThis;
    // This is set here so it can't get removed in config
    @SuppressWarnings("FieldCanBeLocal")
    private final String missingMessage = "Contact Server Admin missing message in config";

    private int setMaximumPoolSize;
    private int setMinimumIdle;
    private int minimumAmount;
    private int cooldown;

    public ConfigManager(KingdomBank plugin) {
        this.plugin = plugin;
        load();
    }

    public void reloadConfigManager(CommandSender sender) {
        plugin.reloadConfig();
        load();
        if (!validate()) {
            sender.sendMessage(Component.text("Config validation failed. Check your config.yml for missing values.").color(NamedTextColor.DARK_RED));
            sender.sendMessage(Component.text("Using default config options if database config info is missing plugin will not work.").color(NamedTextColor.DARK_RED));
            sender.sendMessage(Component.text("Check console for what is missing.").color(NamedTextColor.DARK_RED));
            return;
        }
        sender.sendMessage(Component.text("Config successfully reloaded").color(NamedTextColor.GREEN));
    }

    public void load() {
        host = plugin.getConfig().getString("mariaDB.host", "localhost");
        port = plugin.getConfig().getString("mariaDB.port", "3306");
        database = plugin.getConfig().getString("mariaDB.database", "KingdomBank");
        username = plugin.getConfig().getString("mariaDB.username", "KingdomBank");
        password = plugin.getConfig().getString("mariaDB.password", "password");
        setMaximumPoolSize = plugin.getConfig().getInt("mariaDB.Set-Maximum-Pool-Size", 15);
        setMinimumIdle = plugin.getConfig().getInt("mariaDB.Set-Minimum-Idle", 2);
        balanceMessage = plugin.getConfig().getString("Balance-Message", "Your Balance is $%Bal%.");
        depositMessage = plugin.getConfig().getString("Deposit.Deposit-Message", "You have deposited $%Deposit%.");
        dontHaveEnoughInBalanceDeposit = plugin.getConfig().getString("Deposit.Dont-Have-Enough-In-Balance", "Insufficient funds. You don't have $%Deposit% in your balance.");
        minimumDepositMessage = plugin.getConfig().getString("Deposit.Minimum-Deposit-Message", "Minimum deposit amount is $%Minimum%.");
        withdrawMessage = plugin.getConfig().getString("Withdraw.Withdraw-Message", "You have withdrawn $%Withdraw%.");
        dontHaveEnoughInBalanceWithdraw = plugin.getConfig().getString("Withdraw.Dont-Have-Enough-In-Balance", "Insufficient funds. You don't have $%Withdraw% in your bank balance.");
        minimumWithdrawMessage = plugin.getConfig().getString("Withdraw.Minimum-Withdraw-Message", "Minimum withdraw amount is $%Minimum%.");
        minimumAmount = plugin.getConfig().getInt("Minimum-Amount", 1000);
        cooldown = plugin.getConfig().getInt("Cooldown.Cooldown", 15);
        cooldownMessage = plugin.getConfig().getString("Cooldown.Cooldown-Message", "You must wait %Seconds% seconds before using /bank (subcommands) or /banktop again.");
        invalidAmount = plugin.getConfig().getString("Invalid-Amount", "Invalid amount. Please enter a number greater than zero.");
        noPermission = plugin.getConfig().getString("No-Permission", "You don't have permission to run this command.");
        youCantRunThis = plugin.getConfig().getString("You-Cant-Run-This", "Only Players can run this command.");
    }

    /**
     * This is only for database config as plugin will not work if some of them are wrong
     */
    public boolean validate() {
        List<String> nullFields = new ArrayList<>();

        if (plugin.getConfig().getString("mariaDB.host") == null || plugin.getConfig().getString("mariaDB.host").isEmpty()) nullFields.add("mariaDB.host");
        if (plugin.getConfig().getString("mariaDB.port") == null || plugin.getConfig().getString("mariaDB.port").isEmpty()) nullFields.add("mariaDB.port");
        if (plugin.getConfig().getString("mariaDB.database") == null || plugin.getConfig().getString("mariaDB.database").isEmpty()) nullFields.add("mariaDB.database");
        if (plugin.getConfig().getString("mariaDB.username") == null || plugin.getConfig().getString("mariaDB.username").isEmpty()) nullFields.add("mariaDB.username");
        if (plugin.getConfig().getString("mariaDB.password") == null || plugin.getConfig().getString("mariaDB.password").isEmpty()) nullFields.add("mariaDB.password");
        if (plugin.getConfig().getInt("mariaDB.Set-Maximum-Pool-Size") <= 0) nullFields.add("mariaDB.Set-Maximum-Pool-Size");
        if (plugin.getConfig().getInt("mariaDB.Set-Minimum-Idle") <= 0) nullFields.add("mariaDB.Set-Minimum-Idle");

        if (!nullFields.isEmpty()) {
            plugin.getLogger().warning("Missing or empty config entries: " + String.join(", ", nullFields));
            return false;
        }

        return true;
    }


    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getSetMaximumPoolSize() {
        return setMaximumPoolSize;
    }

    public int getSetMinimumIdle() {
        return setMinimumIdle;
    }

    public String getBalanceMessage() {
        return balanceMessage;
    }

    public String getDepositMessage() {
        return depositMessage;
    }

    public String getDontHaveEnoughInBalanceDeposit() {
        return dontHaveEnoughInBalanceDeposit;
    }

    public String getMinimumDepositMessage() {
        return minimumDepositMessage;
    }

    public String getWithdrawMessage() {
        return withdrawMessage;
    }

    public String getDontHaveEnoughInBalanceWithdraw() {
        return dontHaveEnoughInBalanceWithdraw;
    }

    public String getMinimumWithdrawMessage() {
        return minimumWithdrawMessage;
    }

    public int getMinimumAmount() {
        return minimumAmount;
    }

    public int getCooldown() {
        return cooldown;
    }

    public String getCooldownMessage() {
        return cooldownMessage;
    }

    public String getInvalidAmount() {
        return invalidAmount;
    }

    public  String getNoPermission() {
        return noPermission;
    }

    public String getYouCantRunThis() {
        return youCantRunThis;
    }

    public String getMissingMessage() {
        return missingMessage;
    }
}