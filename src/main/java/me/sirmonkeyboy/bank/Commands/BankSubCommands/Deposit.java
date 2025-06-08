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

import java.sql.SQLException;
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
    public void perform(Player p, String[] args) {

        try {
            // Checks args length
            if (args.length < 2 || args[1].isBlank()) {
                p.sendMessage(Component.text("Use /bank deposit Amount"));
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
            if (!p.hasPermission("Bank.commands.Bank.Deposit")) {
                p.sendMessage(Component.text(configManager.getNoPermission()).color(NamedTextColor.RED));
                return;
            }

            UUID uuid = p.getUniqueId();
            if (cooldownManager.isOnCooldown(uuid)) {
                long seconds = cooldownManager.getRemainingTime(uuid) / 1000;
                String CooldownMessage = configManager.getCooldownMessage().replace("%Seconds%", String.valueOf(seconds));
                p.sendMessage(CooldownMessage);
                return;
            }

            // Checks Deposit amount is over the minimum
            if (!(depositAmount >= DepositMinimum)) {
                String MinimumDepositMessage = configManager.getMinimumDepositMessage().replace("%Minimum%", MinimumDepositAmount);
                p.sendMessage(Component.text(MinimumDepositMessage).color(NamedTextColor.RED));
                return;
            }

            // Checks Deposit amount is less than the players balance
            if (depositAmount > eco.getBalance(p)) {
                String DontHaveEnoughInBalance = configManager.getDontHaveEnoughInBalanceDeposit().replace("%Deposit%", DepositAmountStr);
                p.sendMessage(Component.text(DontHaveEnoughInBalance).color(NamedTextColor.RED));
                return;
            }

            // Deposit Logic
            boolean success = data.depositTransaction(p.getUniqueId(), p.getName(), depositAmount);

            if (!success) {
                p.sendMessage(Component.text("Error in deposit transaction try again").color(NamedTextColor.RED));
                return;
            }

            eco.withdrawPlayer(p, depositAmount);
            String  DepositMessage = configManager.getDepositMessage().replace("%Deposit%", DepositAmountStr);
            p.sendMessage(Component.text(DepositMessage).color(NamedTextColor.GREEN));

            cooldownManager.startCooldown(uuid);
        // Makes sure that the arg is a number
        }catch (NumberFormatException e){
            p.sendMessage(Component.text(configManager.getInvalidAmount()).color(NamedTextColor.RED));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
 }
