package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utils.MariaDB;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class Withdraw extends SubCommand {

    private final Bank plugin;

    private final MariaDB data;

    public Withdraw(Bank plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
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

        try {
            // Checks args length
            if (args.length < 2 || args[1].isBlank()) {
                p.sendMessage(Component.text("Use /bank withdraw Amount"));
                return;
            }
            Economy eco = Bank.getEconomy();

            // Number
            int WithdrawMinimum = Integer.parseInt(Objects.requireNonNull(plugin.getConfig().getString("MinimumAmount")));
            double withdrawAmount = Double.parseDouble(args[1]);

            // Strings
            String WithdrawAmountStr = String.valueOf(withdrawAmount);
            String WithdrawMessage = plugin.getConfig().getString("Withdraw.WithdrawMessage");
            String DontHaveEnoughInBalance = plugin.getConfig().getString("Withdraw.DontHaveEnoughInBalance");
            String MinimumWithdrawMessage = plugin.getConfig().getString("Withdraw.MinimumWithdrawMessage");
            String noPermission = plugin.getConfig().getString("NoPermission");
            String MinimumWithdrawAmount = String.valueOf(WithdrawMinimum);

            // Makes sure players can use the command
            if (!p.hasPermission("Bank.commands.Bank.Withdraw")) {
                if (noPermission == null) {
                    p.sendMessage(Component.text("Contact Server Admin no Permission message").color(NamedTextColor.RED));
                    return;
                }
                p.sendMessage(Component.text(noPermission).color(NamedTextColor.RED));
                return;
            }

            // Checks withdraw amount is over the minimum
            if (!(withdrawAmount >= WithdrawMinimum)) {
                if (MinimumWithdrawMessage == null) {
                    p.sendMessage(Component.text("Contact Server Admin no mini withdraw message").color(NamedTextColor.RED));
                    return;
                }
                MinimumWithdrawMessage = MinimumWithdrawMessage.replace("%Minimum%", MinimumWithdrawAmount);
                p.sendMessage(Component.text(MinimumWithdrawMessage).color(NamedTextColor.RED));
                return;
            }

            // Checks if the withdraw message is in the config
            if (WithdrawMessage == null) {
                p.sendMessage(Component.text("Contact Server Admin no withdraw message").color(NamedTextColor.RED));
                return;
            }

            // Withdraw logic
            boolean success = data.withdrawTransaction(p.getUniqueId(), p.getName(), withdrawAmount);

            if (!success) {
                if (DontHaveEnoughInBalance == null) {
                    p.sendMessage(Component.text("Contact Server Admin no not enough money message").color(NamedTextColor.RED));
                } else {
                    DontHaveEnoughInBalance = DontHaveEnoughInBalance.replace("%Withdraw%", WithdrawAmountStr);
                    p.sendMessage(Component.text(DontHaveEnoughInBalance).color(NamedTextColor.RED));
                }
                return;
            }

            eco.depositPlayer(p, withdrawAmount);

            WithdrawMessage = WithdrawMessage.replace("%Withdraw%", WithdrawAmountStr);
            p.sendMessage(Component.text(WithdrawMessage).color(NamedTextColor.GREEN));

        // Makes sure that the arg is a number
        } catch (NumberFormatException e) {
            p.sendMessage(Component.text("Please enter a number").color(NamedTextColor.RED));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
