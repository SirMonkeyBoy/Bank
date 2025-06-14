package me.sirmonkeyboy.bank.Commands.BankSubCommands;

import me.sirmonkeyboy.bank.KingdomBank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

import java.util.List;
import java.util.UUID;


public class Deposit extends SubCommand {

    private final MariaDB data;

    private final ConfigManager configManager;

    private final CooldownManager cooldownManager;

    public Deposit(MariaDB data, ConfigManager configManager, CooldownManager cooldownManager) {
        this.data = data;
        this.configManager = configManager;
        this.cooldownManager = cooldownManager;
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
    public void perform(Player player, String[] args) {

        try {
            // Checks args length
            if (args.length < 2 || args[1].isBlank()) {
                player.sendMessage(Component.text("Use /bank deposit Amount"));
                return;
            }

            Economy eco = KingdomBank.getEconomy();

            // Numbers
            int DepositMinimum = configManager.getMinimumAmount();
            double depositAmount = Double.parseDouble(args[1]);

            // Strings
            String DepositAmountStr = String.valueOf(depositAmount);
            String MinimumDepositAmount = String.valueOf(DepositMinimum);

            // Makes sure players can use the command
            if (!player.hasPermission("Bank.commands.Bank.Deposit")) {
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

            // Checks Deposit amount is over the minimum
            if (!(depositAmount >= DepositMinimum)) {
                String MinimumDepositMessage = configManager.getMinimumDepositMessage().replace("%Minimum%", MinimumDepositAmount);
                player.sendMessage(Component.text(MinimumDepositMessage).color(NamedTextColor.RED));
                return;
            }

            // Checks Deposit amount is less than the players balance
            if (depositAmount > eco.getBalance(player)) {
                String DontHaveEnoughInBalance = configManager.getDontHaveEnoughInBalanceDeposit().replace("%Deposit%", DepositAmountStr);
                player.sendMessage(Component.text(DontHaveEnoughInBalance).color(NamedTextColor.RED));
                return;
            }

            // Deposit Logic
            data.depositTransaction(uuid,player.getName(), depositAmount, (success) -> {
                if (!success) {
                    player.sendMessage(Component.text("Failed to get balance try again of contact staff.").color(NamedTextColor.RED));
                    return;
                }
                eco.withdrawPlayer(player, depositAmount);
                String DepositMessage = configManager.getDepositMessage().replace("%Deposit%", DepositAmountStr);
                player.sendMessage(Component.text(DepositMessage).color(NamedTextColor.GREEN));

                cooldownManager.startCooldown(uuid);
            });
        // Makes sure that the arg is a number
        }catch (NumberFormatException e){
            player.sendMessage(Component.text(configManager.getInvalidAmount()).color(NamedTextColor.RED));
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
 }
