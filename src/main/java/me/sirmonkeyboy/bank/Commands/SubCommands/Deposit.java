package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Deposit extends SubCommand {

    @Override
    public String getName() {
        return "deposit";
    }

    @Override
    public String getDescription() {
        return "Deposits to your bank";
    }

    @Override
    public String getSyntax() {
        return "/bank deposit";
    }

    @Override
    public void perform(Player p, String[] args) {
        if (p.hasPermission("Bank.commands.Bank.Deposit")) {
            Economy eco = Bank.getEconomy();
            p.sendMessage("test");
        } else {
            if (!p.hasPermission("Bank.commands.Bank.Deposit")) {
                p.sendMessage(translateAlternateColorCodes('&', "&cYou don't have permission to use this command"));
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
