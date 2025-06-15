package me.sirmonkeyboy.bank.Commands;

import me.sirmonkeyboy.bank.KingdomBank;
import me.sirmonkeyboy.bank.Commands.ABankSubCommands.BalOther;
import me.sirmonkeyboy.bank.Commands.ABankSubCommands.Reload;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ABankCommand implements TabExecutor {

    private final List<String> completions = List.of("reload", "balother");

    @SuppressWarnings("FieldCanBeLocal")
    private final KingdomBank plugin;

    @SuppressWarnings("FieldCanBeLocal")
    private final MariaDB data;

    private final ConfigManager configManager;

    @SuppressWarnings("FieldCanBeLocal")
    private final CooldownManager cooldownManager;

    private final ArrayList<SubCommand> subCommands = new ArrayList<>();

    public ABankCommand(KingdomBank plugin, MariaDB data, ConfigManager configManager, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.data = data;
        this.configManager = configManager;
        this.cooldownManager = cooldownManager;
        subCommands.add(new Reload(configManager));
        subCommands.add(new BalOther(plugin, data, configManager, cooldownManager));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (sender instanceof Player player) {
            if (!(player.hasPermission("Bank.commands.ABank"))) {
                player.sendMessage(Component.text(configManager.getNoPermission()).color(NamedTextColor.RED));
                return true;
            }

            if (args.length > 0) {
                for (int i = 0; i < getSubCommands().size(); i++) {
                    if (args[0].equalsIgnoreCase(getSubCommands().get(i).getName())) {
                        try {
                            getSubCommands().get(i).perform(player, args);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return true;
            }

            //noinspection ConstantValue
            if (args.length == 0) {
                player.sendMessage(Component.text("----- Bank usages -----"));
                player.sendMessage(Component.text("/abank reload - ").append(Component.text("Reloads the config file.").color(NamedTextColor.GOLD)));
                player.sendMessage(Component.text("/abank balother - ").append(Component.text("Shows you another players bank balance.").color(NamedTextColor.GOLD)));
            }

            return true;
        }
        return false;
    }

    public ArrayList<SubCommand> getSubCommands() {
        return subCommands;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        List<String> suggestions = new ArrayList<>();

        if (args.length == 1) {
            String input = args[args.length - 1].toLowerCase();
            for (String Sub1 : completions) {
                if (Sub1.startsWith(input)) {
                    suggestions.add(Sub1);
                }
            }
            return suggestions;
        }

        if (args.length >= 2) {
            for (SubCommand sub : subCommands) {
                if (sub.getName().equalsIgnoreCase(args[0]) && commandSender instanceof Player player) {
                    return sub.getSubCommandArguments(player, args);
                }
            }
        }
        return List.of();
    }

}
