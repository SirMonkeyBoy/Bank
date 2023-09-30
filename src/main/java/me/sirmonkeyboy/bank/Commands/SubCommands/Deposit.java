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
                String DepositAmountStr = String.valueOf(DepositAmount);
                String DepositMessage = plugin.getConfig().getString("Deposit.DepositMessage");
                String DontHaveEnoughInBalance = plugin.getConfig().getString("Deposit.DontHaveEnoughInBalance");
                String MinimumDepositMessage = plugin.getConfig().getString("Deposit.MinimumDepositMessage");
                if (DepositAmount >= DepositMinimum){
                    if (DepositAmount <= eco.getBalance(p)) {
                        if (DepositMessage != null){
                            eco.withdrawPlayer(p, DepositAmount);
                            plugin.data.addbalance(p.getUniqueId(), DepositAmount);
                            DepositMessage = DepositMessage.replace("%Deposit%", DepositAmountStr);
                            p.sendMessage(translateAlternateColorCodes('&',DepositMessage));
                        }
                    }else {
                        if (DontHaveEnoughInBalance != null) {
                            DontHaveEnoughInBalance = DontHaveEnoughInBalance.replace("%Deposit%", DepositAmountStr);
                            p.sendMessage(translateAlternateColorCodes('&', DontHaveEnoughInBalance));
                        }
                    }
                }
                else {
                    if (MinimumDepositMessage != null) {
                        MinimumDepositMessage = MinimumDepositMessage.replace("%Minimum%", DepositAmountStr);
                        p.sendMessage(translateAlternateColorCodes('&', MinimumDepositMessage));
                    }
                }
            }catch (NumberFormatException e){
                p.sendMessage("&cPlease deposit a number");
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
