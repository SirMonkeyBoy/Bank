package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class Withdraw extends SubCommand {

    private final Bank plugin;

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
                int WithdrawMinimum = Integer.parseInt(Objects.requireNonNull(plugin.getConfig().getString("MinimumAmount")));
                double WithdrawAmount = Double.parseDouble(args[1]);
                String WithdrawAmountStr = String.valueOf(WithdrawAmount);
                String WithdrawMessage = plugin.getConfig().getString("Withdraw.WithdrawMessage");
                String DontHaveEnoughInBalance = plugin.getConfig().getString("Withdraw.DontHaveEnoughInBalance");
                String MinimumWithdrawMessage = plugin.getConfig().getString("Withdraw.MinimumWithdrawMessage");
                String MinimumWithdrawAmount = String.valueOf(WithdrawMinimum);
                if (WithdrawAmount >= WithdrawMinimum){
                    if (WithdrawAmount == WithdrawMinimum) {
                        if (WithdrawMessage != null){
                            eco.depositPlayer(p, WithdrawAmount);
                            plugin.data.rembalance(p.getUniqueId(), WithdrawAmount);
                            WithdrawMessage = WithdrawMessage.replace("%Withdraw%", WithdrawAmountStr);
                            p.sendMessage(Component.text(WithdrawMessage).color(NamedTextColor.GREEN));
                        }
                    }else {
                        if (DontHaveEnoughInBalance != null) {
                            DontHaveEnoughInBalance = DontHaveEnoughInBalance.replace("%Deposit%", WithdrawAmountStr);
                            p.sendMessage(Component.text(DontHaveEnoughInBalance).color(NamedTextColor.RED));
                        }
                    }
                }
                else {
                    if (MinimumWithdrawMessage != null) {
                        MinimumWithdrawMessage = MinimumWithdrawMessage.replace("%Minimum%", MinimumWithdrawAmount);
                        p.sendMessage(Component.text(MinimumWithdrawMessage).color(NamedTextColor.RED));
                    }
                }
            }catch (NumberFormatException e){
                p.sendMessage(Component.text("Please enter a number").color(NamedTextColor.RED));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (!p.hasPermission("Bank.commands.Bank.Withdraw")) {
                String noPermission = plugin.getConfig().getString("NoPermission");
                if (noPermission != null) {
                    p.sendMessage(Component.text(noPermission).color(NamedTextColor.RED));
                }
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
