package me.sirmonkeyboy.bank.Commands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommands.Balance;
import me.sirmonkeyboy.bank.Commands.SubCommands.Deposit;
import me.sirmonkeyboy.bank.Commands.SubCommands.Withdraw;

import org.bukkit.command.*;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class CommandManager implements TabExecutor {
    @SuppressWarnings("FieldCanBeLocal")
    private final Bank plugin;
    private final ArrayList<SubCommand> subcommands = new ArrayList<>();

    public CommandManager(Bank plugin){
        this.plugin = plugin;
        subcommands.add(new Balance(plugin));
        subcommands.add(new Deposit(plugin));
        subcommands.add(new Withdraw(plugin));
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(sender instanceof Player p) {
            if (p.hasPermission("Bank.commands.Bank")) {
                if (args.length > 0) {
                    for (int i = 0; i < getSubcommands().size(); i++) {
                        if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
                            getSubcommands().get(i).perform(p, args);
                        }
                    }
                } else //noinspection ConstantValue
                    if (args.length == 0) {
                        for (int i = 0; i < getSubcommands().size(); i++) {
                            p.sendMessage("Please use /bank like this");
                            p.sendMessage(getSubcommands().get(i).getSyntax() + " - " + getSubcommands().get(i).getDescription());
                        }
                    }
            }
        }else if (sender instanceof  ConsoleCommandSender c){
            c.sendMessage(translateAlternateColorCodes('&', "&cConsole can't run this command"));
        }else if (sender instanceof BlockCommandSender){
            ConsoleCommandSender c = org.bukkit.Bukkit.getServer().getConsoleSender();
            c.sendMessage(translateAlternateColorCodes('&', "&cCommand Blocks can't run this command"));
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