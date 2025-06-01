package me.sirmonkeyboy.bank.Commands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
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
import java.util.List;

public class BankTop implements TabExecutor {

    private final Bank plugin;

    private final MariaDB data;

    private final ConfigManager configManager;

    public BankTop(Bank plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.data = plugin.data;
        this.configManager = configManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args ) {
        if (sender instanceof Player p) {
            try {
                data.bankTop();
                String[] names = data.getTopPlayers();
                double[] balances = data.getTopBalances();
                p.sendMessage(Component.text(" -----").color(NamedTextColor.YELLOW)
                        .append(Component.text(" Bank Top ").color(NamedTextColor.GOLD))
                        .append(Component.text("-----").color(NamedTextColor.YELLOW)));
                for (int i = 0; i < names.length; i++) {
                    if (names[i] != null) {
                        p.sendMessage((i + 1) + ". " + names[i] + ", $" + balances[i]);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            if (configManager.getYouCantRunThis() != null) {
                sender.sendMessage(Component.text(configManager.getYouCantRunThis()).color(NamedTextColor.RED));
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}
