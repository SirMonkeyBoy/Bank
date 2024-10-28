package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;

import net.kyori.adventure.text.*;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import net.milkbowl.vault.economy.Economy;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;


public class Deposit extends SubCommand {

    private final Bank plugin;

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
                if (args.length >= 2 && !args[1].isBlank()) {
                    int DepositMinimum = Integer.parseInt(Objects.requireNonNull(plugin.getConfig().getString("MinimumAmount")));
                    double DepositAmount = Double.parseDouble(args[1]);
                    String DepositAmountStr = String.valueOf(DepositAmount);
                    String DepositMessage = plugin.getConfig().getString("Deposit.DepositMessage");
                    String DontHaveEnoughInBalance = plugin.getConfig().getString("Deposit.DontHaveEnoughInBalance");
                    String MinimumDepositMessage = plugin.getConfig().getString("Deposit.MinimumDepositMessage");
                    String MinimumDepositAmount = String.valueOf(DepositMinimum);
                    if (DepositAmount >= DepositMinimum) {
                        if (DepositAmount <= eco.getBalance(p)) {
                            if (DepositMessage != null) {
                                eco.withdrawPlayer(p, DepositAmount);
                                plugin.data.addBalance(p.getUniqueId(), DepositAmount);
                                DepositMessage = DepositMessage.replace("%Deposit%", DepositAmountStr);
                                plugin.data.DepositTransaction(p.getUniqueId(), p.getName(), DepositAmount);
                                p.sendMessage(Component.text(DepositMessage).color(NamedTextColor.GREEN));
                            }
                        } else {
                            if (DontHaveEnoughInBalance != null) {
                                DontHaveEnoughInBalance = DontHaveEnoughInBalance.replace("%Deposit%", DepositAmountStr);
                                p.sendMessage(Component.text(DontHaveEnoughInBalance).color(NamedTextColor.RED));
                            }
                        }
                    } else {
                        if (MinimumDepositMessage != null) {
                            MinimumDepositMessage = MinimumDepositMessage.replace("%Minimum%", MinimumDepositAmount);
                            p.sendMessage(Component.text(MinimumDepositMessage).color(NamedTextColor.RED));
                        }
                    }
                } else {
                    p.sendMessage(Component.text("Use /bank deposit Amount"));
                }
            }catch (NumberFormatException e){
                p.sendMessage(Component.text("Please enter a number").color(NamedTextColor.RED));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            if (!p.hasPermission("Bank.commands.Bank.Deposit")) {
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
