package me.sirmonkeyboy.bank.Commands;

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
import java.util.List;
import java.util.UUID;

public class BankTop implements TabExecutor {

    private final MariaDB data;

    private final ConfigManager configManager;

    private final CooldownManager cooldownManager;

    public BankTop(MariaDB data, ConfigManager configManager, CooldownManager cooldownManager) {
        this.data = data;
        this.configManager = configManager;
        this.cooldownManager = cooldownManager;
    }


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args ) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("Bank.commands.Bank.BankTop")) {
                player.sendMessage(Component.text(configManager.getNoPermission()).color(NamedTextColor.RED));
                return true;
            }

            UUID uuid = player.getUniqueId();
            if (cooldownManager.isOnCooldown(uuid)) {
                long seconds = cooldownManager.getRemainingTime(uuid) / 1000;
                String CooldownMessage = configManager.getCooldownMessage().replace("%Seconds%", String.valueOf(seconds));
                player.sendMessage(CooldownMessage);
                return true;
            }

            try {
                data.bankTop();
                String[] names = data.getTopPlayers();
                double[] balances = data.getTopBalances();
                player.sendMessage(Component.text(" -----").color(NamedTextColor.YELLOW)
                        .append(Component.text(" Bank Top ").color(NamedTextColor.GOLD))
                        .append(Component.text("-----").color(NamedTextColor.YELLOW)));
                for (int i = 0; i < names.length; i++) {
                    if (names[i] != null) {
                        player.sendMessage((i + 1) + ". " + names[i] + ", $" + balances[i]);
                    }
                }
                cooldownManager.startCooldown(uuid);
                return true;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else {
            sender.sendMessage(Component.text(configManager.getYouCantRunThis()).color(NamedTextColor.RED));
            return true;
        }
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        return List.of();
    }
}
