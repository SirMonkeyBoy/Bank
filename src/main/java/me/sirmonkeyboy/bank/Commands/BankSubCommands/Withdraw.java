package me.sirmonkeyboy.bank.Commands.BankSubCommands;

import me.sirmonkeyboy.bank.KingdomBank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

import java.util.List;
import java.util.UUID;

public class Withdraw extends SubCommand {

    private final MariaDB data;

    private final ConfigManager configManager;

    private final CooldownManager cooldownManager;

    public Withdraw(MariaDB data, ConfigManager configManager, CooldownManager cooldownManager) {
        this.data = data;
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
    public void perform(Player player, String[] args) {

        try {
            // Checks args length
            if (args.length < 2 || args[1].isBlank()) {
                player.sendMessage(Component.text("Use /bank withdraw Amount"));
                return;
            }

            Economy eco = KingdomBank.getEconomy();

            // Number
            int WithdrawMinimum = configManager.getMinimumAmount();
            double withdrawAmount = Double.parseDouble(args[1]);

            // Strings
            String WithdrawAmountStr = String.valueOf(withdrawAmount);
            String MinimumWithdrawAmount = String.valueOf(WithdrawMinimum);

            // Makes sure players can use the command
            if (!player.hasPermission("Bank.commands.Bank.Withdraw")) {
                player.sendMessage(Component.text(configManager.getNoPermission()).color(NamedTextColor.RED));
                return;
            }

            UUID uuid = player.getUniqueId();
            if (cooldownManager.isOnCooldown(uuid)) {
                long seconds = cooldownManager.getRemainingTime(uuid) / 1000;
                String CooldownMessage = configManager.getCooldownMessage().replace("%Seconds%", String.valueOf(seconds));
                player.sendMessage(CooldownMessage);
                return;
            }

            // Checks withdraw amount is over the minimum
            if (!(withdrawAmount >= WithdrawMinimum)) {
                String MinimumWithdrawMessage = configManager.getMinimumWithdrawMessage().replace("%Minimum%", MinimumWithdrawAmount);
                player.sendMessage(Component.text(MinimumWithdrawMessage).color(NamedTextColor.RED));
                return;
            }

            // Withdraw logic
            data.withdrawTransaction(uuid,player.getName(), withdrawAmount, (success) -> {
                if (!success) {
                    player.sendMessage(Component.text("Failed to get balance try again of contact staff.").color(NamedTextColor.RED));
                    return;
                }
                eco.depositPlayer(player, withdrawAmount);

                String withdrawMessage = configManager.getWithdrawMessage().replace("%Withdraw%", WithdrawAmountStr);
                player.sendMessage(Component.text(withdrawMessage).color(NamedTextColor.GREEN));
                cooldownManager.startCooldown(uuid);
            });
        // Makes sure that the arg is a number
        } catch (NumberFormatException e) {
            player.sendMessage(Component.text(configManager.getInvalidAmount()).color(NamedTextColor.RED));
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
