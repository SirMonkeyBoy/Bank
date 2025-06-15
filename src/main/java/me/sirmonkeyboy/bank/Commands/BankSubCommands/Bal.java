package me.sirmonkeyboy.bank.Commands.BankSubCommands;

import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class Bal extends SubCommand {

    private final MariaDB data;

    private final ConfigManager configManager;

    private final CooldownManager cooldownManager;

    public Bal(MariaDB data, ConfigManager configManager, CooldownManager cooldownManager) {
        this.data = data;
        this.configManager = configManager;
        this.cooldownManager = cooldownManager;
    }

    @Override
    public String getName() {
        return "bal";
    }

    @Override
    public String getDescription() {
        return "Your Bank Balance";
    }

    @Override
    public String getSyntax() {
        return "/bank bal";
    }

    @Override
    public void perform(Player player, String[] args) {
        if (!player.hasPermission("Bank.commands.Bank.Balance")) {
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

        data.getBalance(player, (success, balance) -> {
            if (!success) {
                player.sendMessage(Component.text("Failed to get balance try again of contact staff.").color(NamedTextColor.RED));
                return;
            }
            String msg = configManager.getBalanceMessage().replace("%Bal%", String.valueOf(balance));
            player.sendMessage(Component.text(msg).color(NamedTextColor.GREEN));
            cooldownManager.startCooldown(uuid);
        });
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
