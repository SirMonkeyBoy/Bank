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
    private String BalanceMessage;
    private String DepositMessage;
    private String DontHaveEnoughInBalanceDeposit;
    private String MinimumDepositMessage;
    private String WithdrawMessage;
    private String DontHaveEnoughInBalanceWithdraw;
    private String MinimumWithdrawMessage;
    private String NoPermission;
    private String YouCantRunThis;
    private String CooldownMessage;
    // This is set here so it can't get removed in config
    @SuppressWarnings("FieldCanBeLocal")
    private final String MissingMessage = "Contact Server Admin missing message in config";

    private int SetMaximumPoolSize;
    private int SetMinimumIdle;
    private int MinimumAmount;
    private int Cooldown;


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
        SetMaximumPoolSize = plugin.getConfig().getInt("mariaDB.Set-Maximum-Pool-Size");
        SetMinimumIdle = plugin.getConfig().getInt("mariaDB.Set-Minimum-Idle");
        BalanceMessage = plugin.getConfig().getString("Balance-Message");
        DepositMessage = plugin.getConfig().getString("Deposit.Deposit-Message");
        DontHaveEnoughInBalanceDeposit = plugin.getConfig().getString("Deposit.Dont-Have-Enough-In-Balance");
        MinimumDepositMessage = plugin.getConfig().getString("Deposit.Minimum-Deposit-Message");
        WithdrawMessage = plugin.getConfig().getString("Withdraw.Withdraw-Message");
        DontHaveEnoughInBalanceWithdraw = plugin.getConfig().getString("Withdraw.Dont-Have-Enough-In-Balance");
        MinimumWithdrawMessage = plugin.getConfig().getString("Withdraw.Minimum-Withdraw-Message");
        MinimumAmount = plugin.getConfig().getInt("Minimum-Amount");
        Cooldown = plugin.getConfig().getInt("Cooldown.Cooldown");
        CooldownMessage = plugin.getConfig().getString("Cooldown.Cooldown-Message");
        NoPermission = plugin.getConfig().getString("No-Permission");
        YouCantRunThis = plugin.getConfig().getString("You-Cant-Run-This");
    }

    public boolean validate() {
        List<String> nullFields = new ArrayList<>();

        if (host == null || host.isEmpty()) nullFields.add("mariaDB.host");
        if (port == null || port.isEmpty()) nullFields.add("mariaDB.port");
        if (database == null || database.isEmpty()) nullFields.add("mariaDB.database");
        if (username == null || username.isEmpty()) nullFields.add("mariaDB.username");
        if (password == null || password.isEmpty()) nullFields.add("mariaDB.password");
        if (SetMaximumPoolSize <= 0) nullFields.add("mariaDB.Set-Maximum-Pool-Size");
        if (SetMinimumIdle <= 0) nullFields.add("mariaDB.Set-Minimum-Idle");
        if (BalanceMessage == null || BalanceMessage.isEmpty()) nullFields.add("Balance-Message");
        if (DepositMessage == null || DepositMessage.isEmpty()) nullFields.add("Deposit.Deposit-Message");
        if (DontHaveEnoughInBalanceDeposit == null || DontHaveEnoughInBalanceDeposit.isEmpty()) nullFields.add("Deposit.Dont-Have-Enough-In-Balance");
        if (MinimumDepositMessage == null || MinimumDepositMessage.isEmpty()) nullFields.add("Deposit.Minimum-Deposit-Message");
        if (WithdrawMessage == null || WithdrawMessage.isEmpty()) nullFields.add("Withdraw.Withdraw-Message");
        if (DontHaveEnoughInBalanceWithdraw == null || DontHaveEnoughInBalanceWithdraw.isEmpty()) nullFields.add("Withdraw.Dont-Have-Enough-In-Balance");
        if (MinimumWithdrawMessage == null || MinimumWithdrawMessage.isEmpty()) nullFields.add("Withdraw.Minimum-Withdraw-Message");
        if (MinimumAmount <= 0) nullFields.add("Minimum-Amount");
        if (Cooldown <= 0) nullFields.add("Cooldown.Cooldown");
        if (CooldownMessage == null || CooldownMessage.isEmpty()) nullFields.add("Cooldown.Cooldown-Message");
        if (NoPermission == null || NoPermission.isEmpty()) nullFields.add("No-Permission");
        if (YouCantRunThis == null || YouCantRunThis.isEmpty()) nullFields.add("You-Cant-Run-This");

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
        return SetMaximumPoolSize;
    }

    public int getSetMinimumIdle() {
        return SetMinimumIdle;
    }

    public String getBalanceMessage() {
        return BalanceMessage;
    }

    public String getDepositMessage() {
        return DepositMessage;
    }

    public String getDontHaveEnoughInBalanceDeposit() {
        return DontHaveEnoughInBalanceDeposit;
    }

    public String getMinimumDepositMessage() {
        return MinimumDepositMessage;
    }

    public String getWithdrawMessage() {
        return WithdrawMessage;
    }

    public String getDontHaveEnoughInBalanceWithdraw() {
        return DontHaveEnoughInBalanceWithdraw;
    }

    public String getMinimumWithdrawMessage() {
        return  MinimumWithdrawMessage;
    }

    public int getMinimumAmount() {
        return MinimumAmount;
    }

    public int getCooldown() {
        return Cooldown;
    }

    public String getCooldownMessage() {
        return CooldownMessage;
    }

    public  String getNoPermission() {
        return  NoPermission;
    }

    public String getYouCantRunThis() {
        return YouCantRunThis;
    }

    public String getMissingMessage() {
        return  MissingMessage;
    }
}