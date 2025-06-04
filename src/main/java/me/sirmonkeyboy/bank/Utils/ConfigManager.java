package me.sirmonkeyboy.bank.Utils;

import me.sirmonkeyboy.bank.Bank;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.ArrayList;

public class ConfigManager {

    private final Bank plugin;

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

    public ConfigManager(Bank plugin) {
        this.plugin = plugin;
        load();
    }

    public void reloadConfigManager(CommandSender sender) {
        plugin.reloadConfig();
        load();
        if (!validate()) {
            sender.sendMessage(Component.text("Config validation failed. Check your config.yml for missing values.").color(NamedTextColor.DARK_RED));
            sender.sendMessage(Component.text("Plugin will not fully work with out it").color(NamedTextColor.DARK_RED));
            sender.sendMessage(Component.text("Check console for what is missing").color(NamedTextColor.DARK_RED));
            return;
        }
        sender.sendMessage(Component.text("Config successfully reloaded").color(NamedTextColor.GREEN));
    }

    public void load() {
        host = plugin.getConfig().getString("mariaDB.host");
        port = plugin.getConfig().getString("mariaDB.port");
        database = plugin.getConfig().getString("mariaDB.database");
        username = plugin.getConfig().getString("mariaDB.username");
        password = plugin.getConfig().getString("mariaDB.password");
        setMaximumPoolSize = plugin.getConfig().getInt("mariaDB.Set-Maximum-Pool-Size");
        setMinimumIdle = plugin.getConfig().getInt("mariaDB.Set-Minimum-Idle");
        balanceMessage = plugin.getConfig().getString("Balance-Message");
        depositMessage = plugin.getConfig().getString("Deposit.Deposit-Message");
        dontHaveEnoughInBalanceDeposit = plugin.getConfig().getString("Deposit.Dont-Have-Enough-In-Balance");
        minimumDepositMessage = plugin.getConfig().getString("Deposit.Minimum-Deposit-Message");
        withdrawMessage = plugin.getConfig().getString("Withdraw.Withdraw-Message");
        dontHaveEnoughInBalanceWithdraw = plugin.getConfig().getString("Withdraw.Dont-Have-Enough-In-Balance");
        minimumWithdrawMessage = plugin.getConfig().getString("Withdraw.Minimum-Withdraw-Message");
        minimumAmount = plugin.getConfig().getInt("Minimum-Amount");
        cooldown = plugin.getConfig().getInt("Cooldown.Cooldown");
        cooldownMessage = plugin.getConfig().getString("Cooldown.Cooldown-Message");
        invalidAmount = plugin.getConfig().getString("Invalid-Amount");
        noPermission = plugin.getConfig().getString("No-Permission");
        youCantRunThis = plugin.getConfig().getString("You-Cant-Run-This");
    }

    public boolean validate() {
        List<String> nullFields = new ArrayList<>();

        if (host == null || host.isEmpty()) nullFields.add("mariaDB.host");
        if (port == null || port.isEmpty()) nullFields.add("mariaDB.port");
        if (database == null || database.isEmpty()) nullFields.add("mariaDB.database");
        if (username == null || username.isEmpty()) nullFields.add("mariaDB.username");
        if (password == null || password.isEmpty()) nullFields.add("mariaDB.password");
        if (setMaximumPoolSize <= 0) nullFields.add("mariaDB.Set-Maximum-Pool-Size");
        if (setMinimumIdle <= 0) nullFields.add("mariaDB.Set-Minimum-Idle");
        if (balanceMessage == null || balanceMessage.isEmpty()) nullFields.add("Balance-Message");
        if (depositMessage == null || depositMessage.isEmpty()) nullFields.add("Deposit.Deposit-Message");
        if (dontHaveEnoughInBalanceDeposit == null || dontHaveEnoughInBalanceDeposit.isEmpty()) nullFields.add("Deposit.Dont-Have-Enough-In-Balance");
        if (minimumDepositMessage == null || minimumDepositMessage.isEmpty()) nullFields.add("Deposit.Minimum-Deposit-Message");
        if (withdrawMessage == null || withdrawMessage.isEmpty()) nullFields.add("Withdraw.Withdraw-Message");
        if (dontHaveEnoughInBalanceWithdraw == null || dontHaveEnoughInBalanceWithdraw.isEmpty()) nullFields.add("Withdraw.Dont-Have-Enough-In-Balance");
        if (minimumWithdrawMessage == null || minimumWithdrawMessage.isEmpty()) nullFields.add("Withdraw.Minimum-Withdraw-Message");
        if (minimumAmount <= 0) nullFields.add("Minimum-Amount");
        if (cooldown <= 0) nullFields.add("Cooldown.Cooldown");
        if (cooldownMessage == null || cooldownMessage.isEmpty()) nullFields.add("Cooldown.Cooldown-Message");
        if (invalidAmount == null || invalidAmount.isEmpty()) nullFields.add("Invalid-Amount");
        if (noPermission == null || noPermission.isEmpty()) nullFields.add("No-Permission");
        if (youCantRunThis == null || youCantRunThis.isEmpty()) nullFields.add("You-Cant-Run-This");

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