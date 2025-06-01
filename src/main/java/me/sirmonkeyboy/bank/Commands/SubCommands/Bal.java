package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;


public class Bal extends SubCommand {

    private final Bank plugin;

    private final MariaDB data;

    private final ConfigManager configManager;

    public Bal(Bank plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.configManager = configManager;
    }

    @Override
    public String getName() {
        return "bal";
    }

    @Override
    public String getDescription() {
        return "Your Bank Balance";
    }

    @Override
    public String getSyntax() {
        return "/bank bal";
    }

    @Override
    public void perform(Player p, String[] args) throws SQLException {
        if (p.hasPermission("Bank.commands.Bank.Balance")) {
            double balance = data.getBalance(p.getUniqueId());
            if (configManager.getBalanceMessage() != null) {
                String BalanceStr = String.valueOf(balance);
                String BalanceMessage = configManager.getBalanceMessage().replace("%Bal%", BalanceStr);
                p.sendMessage(Component.text(BalanceMessage).color(NamedTextColor.GREEN));
            }
        } else {
            if (!p.hasPermission("Bank.commands.Bank.Balance")) {
                if (configManager.getNoPermission() != null) {
                    p.sendMessage(Component.text(configManager.getNoPermission()).color(NamedTextColor.RED));
                }
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
