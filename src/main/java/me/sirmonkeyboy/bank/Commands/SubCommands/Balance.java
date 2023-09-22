package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Commands.SubCommand;
import org.bukkit.entity.Player;

import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Balance extends SubCommand {

    @Override
    public String getName() {
        return "balance";
    }

    @Override
    public String getDescription() {
        return "Your Bank Balance";
    }

    @Override
    public String getSyntax() {
        return "/bank balance";
    }

    @Override
    public void perform(Player p, String[] args) {
        if (p.hasPermission("Bank.commands.Bank.Balance")) {
            p.sendMessage("test");
        } else {
            if (!p.hasPermission("Bank.commands.Bank.Balance")) {
                p.sendMessage(translateAlternateColorCodes('&', "&cYou don't have permission to use this command"));
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
