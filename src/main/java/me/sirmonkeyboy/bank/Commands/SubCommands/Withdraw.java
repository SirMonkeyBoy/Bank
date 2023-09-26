package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;

import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Withdraw extends SubCommand {
    @Override
    public String getName() {
        return "withdraw";
    }

    @Override
    public String getDescription() {
        return "Withdraw form your bank";
    }

    @Override
    public String getSyntax() {
        return "/bank withdraw (Amount)";
    }

    @Override
    public void perform(Player p, String[] args) {
        if (p.hasPermission("Bank.commands.Bank.Withdraw")) {
            Economy eco = Bank.getEconomy();
            p.sendMessage("test");
        } else {
            if (!p.hasPermission("Bank.commands.Bank.Withdraw")) {
                p.sendMessage(translateAlternateColorCodes('&', "&cYou don't have permission to use this command"));
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
