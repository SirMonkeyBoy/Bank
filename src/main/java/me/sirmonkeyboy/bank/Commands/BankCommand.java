package me.sirmonkeyboy.bank.Commands;

import me.sirmonkeyboy.bank.Commands.BankSubCommands.*;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankCommand implements TabExecutor {
    @SuppressWarnings("FieldCanBeLocal")
    private final MariaDB data;

    private final ConfigManager configManager;

    @SuppressWarnings("FieldCanBeLocal")
    private final CooldownManager cooldownManager;

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public BankCommand(MariaDB data, ConfigManager configManager, CooldownManager cooldownManager){
        this.data = data;
        this.configManager = configManager;
        this.cooldownManager = cooldownManager;
        subcommands.add(new Balance(data, configManager, cooldownManager));
        subcommands.add(new Deposit(data, configManager, cooldownManager));
        subcommands.add(new Withdraw(data, configManager, cooldownManager));
        subcommands.add(new Bal(data, configManager,cooldownManager));
        subcommands.add(new Help());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player player) {
            if (!player.hasPermission("Bank.commands.Bank")) {
                player.sendMessage(Component.text(configManager.getNoPermission()).color(NamedTextColor.RED));
                return true;
            }

            if (args.length > 0) {
                for (int i = 0; i < getSubcommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                        try {
                            getSubcommands().get(i).perform(player, args);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return true;
            }

            //noinspection ConstantValue
            if (args.length == 0) {
                player.sendMessage(Component.text("Bank usages"));
                player.sendMessage(Component.text("/bank balance or /bank bal - ").append(Component.text("Gets your bank balance.").color(NamedTextColor.GOLD)));
                player.sendMessage(Component.text("/bank deposit (Amount) - ").append(Component.text("Deposits (Amount) into your account.").color(NamedTextColor.GOLD)));
                player.sendMessage(Component.text("/bank withdraw (Amount) - ").append(Component.text("Withdraws (Amount) from your account.").color(NamedTextColor.GOLD)));
                return true;
            }
        }else if (sender instanceof  ConsoleCommandSender c){
            c.sendMessage(Component.text("Console can't run this command").color(NamedTextColor.RED));
        }else if (sender instanceof BlockCommandSender){
            ConsoleCommandSender c = org.bukkit.Bukkit.getServer().getConsoleSender();
            c.sendMessage(Component.text("Command Blocks can't run this command").color(NamedTextColor.RED));
        }
        return true;
    }

    public ArrayList<SubCommand> getSubcommands(){
        return subcommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> subcommandArguments = new ArrayList<>();

            for (int i = 0; i < getSubcommands().size(); i++){
                subcommandArguments.add(getSubcommands().get(i).getName());
            }
            return subcommandArguments;
        }else if (args.length >= 2){
            for (int i = 0; i < getSubcommands().size(); i++) {
                if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                    return getSubcommands().get(i).getSubCommandArguments((Player) sender, args);
                }
            }
        }
        return null;
    }
}