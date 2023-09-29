package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

import java.util.List;
import java.util.Objects;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Withdraw extends SubCommand {

    private Bank plugin;

    public Withdraw(Bank plugin) {
        this.plugin = plugin;
    }

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
            try {
                int DepositMinimum = Integer.parseInt(Objects.requireNonNull(plugin.getConfig().getString("MinimumAmount")));
                int WithdrawAmount = Integer.parseInt(args[1]);
                if (WithdrawAmount >= DepositMinimum){
                    String WithdrawMessage = plugin.getConfig().getString("WithdrawMessage");
                    if (WithdrawAmount <= plugin.data.getbalance(p.getUniqueId())) {
                        if (WithdrawMessage != null){
                            eco.depositPlayer(p, WithdrawAmount);
                            plugin.data.rembalance(p.getUniqueId(), WithdrawAmount);
                            String WithdrawAmountStr = String.valueOf(WithdrawAmount);
                            WithdrawMessage = WithdrawMessage.replace("%Withdraw%", WithdrawAmountStr);
                            p.sendMessage(translateAlternateColorCodes('&',WithdrawMessage));
                        }
                    }else {
                        p.sendMessage(translateAlternateColorCodes('&',"You don't have $" + WithdrawAmount + " in your bank"));
                    }
                }
                else {
                    p.sendMessage(translateAlternateColorCodes('&',"Minimum withdraw amount is $1000"));
                }
            }catch (NumberFormatException e){
                p.sendMessage("&cPlease deposit a number");
            }
        } else {
            if (!p.hasPermission("Bank.commands.Bank.Withdraw")) {
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
