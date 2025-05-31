package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utill.MariaDB;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;
import java.util.List;


public class Deposit extends SubCommand {

    private final Bank plugin;

    private final MariaDB data;

    public Deposit(Bank plugin) {
        this.plugin = plugin;
        this.data = plugin.data;
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

        try {
            // Checks args length
            if (args.length < 2 || args[1].isBlank()) {
                p.sendMessage(Component.text("Use /bank deposit Amount"));
                return;
            }

            Economy eco = Bank.getEconomy();

            // Numbers
            int DepositMinimum = plugin.getConfig().getInt("MinimumAmount");
            double depositAmount = Double.parseDouble(args[1]);

            // Strings
            String DepositAmountStr = String.valueOf(depositAmount);
            String DepositMessage = plugin.getConfig().getString("Deposit.DepositMessage");
            String DontHaveEnoughInBalance = plugin.getConfig().getString("Deposit.DontHaveEnoughInBalance");
            String MinimumDepositMessage = plugin.getConfig().getString("Deposit.MinimumDepositMessage");
            String MinimumDepositAmount = String.valueOf(DepositMinimum);
            String noPermission = plugin.getConfig().getString("NoPermission");

            // Makes sure players can use the command
            if (!p.hasPermission("Bank.commands.Bank.Deposit")) {
                if (noPermission == null) {
                    p.sendMessage(Component.text("Contact Server Admin no Permission message").color(NamedTextColor.RED));
                    return;
                }
                p.sendMessage(Component.text(noPermission).color(NamedTextColor.RED));
                return;
            }

            // Checks Deposit amount is over the minimum
            if (!(depositAmount >= DepositMinimum)) {
                if (MinimumDepositMessage == null) {
                    p.sendMessage(Component.text("Contact Server Admin no mini deposit message").color(NamedTextColor.RED));
                    return;
                }
                MinimumDepositMessage = MinimumDepositMessage.replace("%Minimum%", MinimumDepositAmount);
                p.sendMessage(Component.text(MinimumDepositMessage).color(NamedTextColor.RED));
                return;
            }

            // Checks Deposit amount is less than the players balance
            if (depositAmount > eco.getBalance(p)) {
                if (DontHaveEnoughInBalance == null) {
                    p.sendMessage(Component.text("Contact Server Admin no not enough money message").color(NamedTextColor.RED));
                    return;
                }
                DontHaveEnoughInBalance = DontHaveEnoughInBalance.replace("%Deposit%", DepositAmountStr);
                p.sendMessage(Component.text(DontHaveEnoughInBalance).color(NamedTextColor.RED));
                return;
            }

            // Checks if the deposit message is in the config
            if (DepositMessage == null) {
                p.sendMessage(Component.text("Contact Server Admin no deposit message").color(NamedTextColor.RED));
                return;
            }

            // Deposit Logic
            eco.withdrawPlayer(p, depositAmount);
            data.addBalance(p.getUniqueId(), p.getName(), depositAmount);
            DepositMessage = DepositMessage.replace("%Deposit%", DepositAmountStr);
            p.sendMessage(Component.text(DepositMessage).color(NamedTextColor.GREEN));

        // Makes sure that the arg is a number
        }catch (NumberFormatException e){
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
