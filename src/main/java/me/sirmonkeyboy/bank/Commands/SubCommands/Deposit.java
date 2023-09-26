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
        return "/bank deposit (Amount)";
    }

    @Override
    public void perform(Player p, String[] args) {
        if (p.hasPermission("Bank.commands.Bank.Deposit")) {
            Economy eco = Bank.getEconomy();
            try {
                int DepositMinimum = 1000;
                int DepositAmount = Integer.parseInt(args[1]);
                if (DepositAmount >= DepositMinimum){
                    if (DepositAmount <= eco.getBalance(p)){
                        p.sendMessage("Deposited $" + DepositAmount);
                    }else {
                        p.sendMessage("You Don't have $" + DepositAmount + " in your balance");
                    }
                }
                else {
                    p.sendMessage("Minimum deposit amount is $1000");
                }
            }catch (NumberFormatException e){
                p.sendMessage("Minimum deposit amount is $1000");
            }
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
