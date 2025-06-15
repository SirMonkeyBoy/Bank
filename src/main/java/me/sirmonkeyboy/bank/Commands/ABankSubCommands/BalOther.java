package me.sirmonkeyboy.bank.Commands.ABankSubCommands;

import me.sirmonkeyboy.bank.KingdomBank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;
import me.sirmonkeyboy.bank.Utils.MariaDB;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public class BalOther extends SubCommand {

    private final KingdomBank plugin;

    private final MariaDB data;

    private final ConfigManager configManager;

    private final CooldownManager cooldownManager;

    public BalOther(KingdomBank plugin, MariaDB data, ConfigManager configManager, CooldownManager cooldownManager) {
        this.plugin = plugin;
        this.data = data;
        this.configManager = configManager;
        this.cooldownManager = cooldownManager;
    }


    @Override
    public String getName() {
        return "balother";
    }

    @Override
    public String getDescription() {
        return "Tells you the bank balance of another player";
    }

    @Override
    public String getSyntax() {
        return "/aback balother name";
    }

    @Override
    public void perform(Player player, String[] args) {

        if (args.length < 2 || args[1].isBlank()) {
            player.sendMessage(Component.text("Use /abank balother name"));
            return;
        }

        String name = args[1];

        UUID uuid = player.getUniqueId();
        if (cooldownManager.isOnCooldown(uuid)) {
            long seconds = cooldownManager.getRemainingTime(uuid) / 1000;
            String CooldownMessage = configManager.getCooldownMessage().replace("%Seconds%", String.valueOf(seconds));
            player.sendMessage(CooldownMessage);
            return;
        }


        data.getBalanceOther(name, (success, balance) -> {
            if (!success) {
                player.sendMessage(Component.text("Failed to get the balance is the name correct.").color(NamedTextColor.RED));
                return;
            }
            player.sendMessage(Component.text(name + "'s balance is " + balance).color(NamedTextColor.GREEN));
            cooldownManager.startCooldown(uuid);
        });
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        if (args.length == 2) {
            return plugin.getServer().getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[1].toLowerCase()))
                    .toList();
        }
        return List.of();
    }
}
