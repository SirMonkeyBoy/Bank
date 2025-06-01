package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class Withdraw extends SubCommand {

    private final Bank plugin;

    private MariaDB data;

    private final ConfigManager configManager;

    private final CooldownManager cooldownManager;

    public Withdraw(Bank plugin, ConfigManager configManager, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.configManager = configManager;
        this.cooldownManager = cooldownManager;
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
            int WithdrawMinimum = configManager.getMinimumAmount();
            double withdrawAmount = Double.parseDouble(args[1]);

            // Strings
            String WithdrawAmountStr = String.valueOf(withdrawAmount);
            String MinimumWithdrawAmount = String.valueOf(WithdrawMinimum);

            // Makes sure players can use the command
            if (!p.hasPermission("Bank.commands.Bank.Withdraw")) {
                if (configManager.getNoPermission() == null) {
                    p.sendMessage(Component.text(configManager.getMissingMessage()).color(NamedTextColor.RED));
                    return;
                }
                p.sendMessage(Component.text(configManager.getNoPermission()).color(NamedTextColor.RED));
                return;
            }

            UUID uuid = p.getUniqueId();
            if (cooldownManager.isOnCooldown(uuid)) {
                long seconds = cooldownManager.getRemainingTime(uuid) / 1000;
                p.sendMessage("You're on cooldown! Try again in " + seconds + " seconds.");
                return;
            }

            // Checks withdraw amount is over the minimum
            if (!(withdrawAmount >= WithdrawMinimum)) {
                if (configManager.getMinimumWithdrawMessage() == null) {
                    p.sendMessage(Component.text(configManager.getMissingMessage()).color(NamedTextColor.RED));
                    return;
                }
                String MinimumWithdrawMessage = configManager.getMinimumWithdrawMessage().replace("%Minimum%", MinimumWithdrawAmount);
                p.sendMessage(Component.text(MinimumWithdrawMessage).color(NamedTextColor.RED));
                return;
            }

            // Checks if the withdraw message is in the config
            if (configManager.getWithdrawMessage() == null) {
                p.sendMessage(Component.text(configManager.getMissingMessage()).color(NamedTextColor.RED));
                return;
            }

            // Withdraw logic
            boolean success = data.withdrawTransaction(p.getUniqueId(), p.getName(), withdrawAmount);

            if (!success) {
                if (configManager.getDontHaveEnoughInBalanceWithdraw() == null) {
                    p.sendMessage(Component.text(configManager.getMissingMessage()).color(NamedTextColor.RED));
                    return;
                }
                String DontHaveEnoughInBalance = configManager.getDontHaveEnoughInBalanceWithdraw().replace("%Withdraw%", WithdrawAmountStr);
                p.sendMessage(Component.text(DontHaveEnoughInBalance + "or error in withdraw transaction try again").color(NamedTextColor.RED));

                return;
            }

            eco.depositPlayer(p, withdrawAmount);

            String WithdrawMessage = configManager.getWithdrawMessage().replace("%Withdraw%", WithdrawAmountStr);
            p.sendMessage(Component.text(WithdrawMessage).color(NamedTextColor.GREEN));

            cooldownManager.startCooldown(uuid);
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
