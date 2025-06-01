package me.sirmonkeyboy.bank.Commands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommands.*;
import me.sirmonkeyboy.bank.Utils.ConfigManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {
    @SuppressWarnings("FieldCanBeLocal")
    private final Bank plugin;

    private final ConfigManager configManager;

    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager(Bank plugin, ConfigManager configManager){
        this.plugin = plugin;
        this.configManager = configManager;
        subcommands.add(new Balance(plugin, configManager));
        subcommands.add(new Deposit(plugin, configManager));
        subcommands.add(new Withdraw(plugin, configManager));
        subcommands.add(new Bal(plugin, configManager));
        subcommands.add(new Help());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player p) {
            if (p.hasPermission("Bank.commands.Bank")) {
                if (args.length > 0) {
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                            try {
                                getSubcommands().get(i).perform(p, args);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else //noinspection ConstantValue
                    if (args.length == 0) {
                        p.sendMessage(Component.text("Bank usages"));
                        p.sendMessage(Component.text("/bank balance or /bank bal - ").append(Component.text("Gets your bank balance").color(NamedTextColor.GOLD)));
                        p.sendMessage(Component.text("/bank deposit (Amount) - ").append(Component.text("Deposits (Amount) into your account").color(NamedTextColor.GOLD)));
                        p.sendMessage(Component.text("/bank withdraw (Amount) - ").append(Component.text("Withdraws (Amount) from your account").color(NamedTextColor.GOLD)));
                    }
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