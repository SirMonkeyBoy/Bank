package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

import java.util.List;
import java.util.Objects;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Deposit extends SubCommand {

    private Bank plugin;

    public Deposit(Bank plugin) {
        this.plugin = plugin;
    }

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
                int DepositMinimum = Integer.parseInt(Objects.requireNonNull(plugin.getConfig().getString("MinimumAmount")));
                int DepositAmount = Integer.parseInt(args[1]);
                if (DepositAmount >= DepositMinimum){
                    String DepositMessage = plugin.getConfig().getString("DepositMessage");
                    if (DepositAmount <= eco.getBalance(p)) {
                        if (DepositMessage != null){
                            eco.withdrawPlayer(p, DepositAmount);
                            plugin.data.addbalance(p.getUniqueId(), DepositAmount);
                            p.sendMessage(translateAlternateColorCodes('&',DepositMessage + DepositAmount));
                        }
                    }else {
                        p.sendMessage(translateAlternateColorCodes('&',"You don't have $" + DepositAmount + " in your balance"));
                    }
                }
                else {
                    p.sendMessage(translateAlternateColorCodes('&',"Minimum deposit amount is $1000"));
                }
            }catch (NumberFormatException e){
                p.sendMessage("Please deposit a number");
            }
        } else {
            if (!p.hasPermission("Bank.commands.Bank.Deposit")) {
                String noPermission = plugin.getConfig().getString("NoPermission");
                if (noPermission != null) {
                    p.sendMessage(translateAlternateColorCodes('&', noPermission));
                }
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
