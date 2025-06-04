package me.sirmonkeyboy.bank.Commands.ABankSubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import me.sirmonkeyboy.bank.Utils.CooldownManager;

import me.sirmonkeyboy.bank.Utils.MariaDB;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;
import java.util.OptionalDouble;
import java.util.UUID;

public class BalOther extends SubCommand {

    private final Bank plugin;

    private final MariaDB data;

    private final ConfigManager configManager;

    private final CooldownManager cooldownManager;

    public BalOther(Bank plugin, MariaDB data, ConfigManager configManager, CooldownManager cooldownManager) {
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
    public void perform(Player p, String[] args) throws SQLException {

        if (args.length < 2 || args[1].isBlank()) {
            p.sendMessage(Component.text("Use /abank balother name"));
            return;
        }

        String name = args[1];

        UUID uuid = p.getUniqueId();
        if (cooldownManager.isOnCooldown(uuid)) {
            long seconds = cooldownManager.getRemainingTime(uuid) / 1000;
            String CooldownMessage = configManager.getCooldownMessage().replace("%Seconds%", String.valueOf(seconds));
            p.sendMessage(CooldownMessage);
            return;
        }

        OptionalDouble result = data.getBalanceOther(name);

        if (result.isEmpty()) {
            p.sendMessage(Component.text("No player found").color(NamedTextColor.RED));
            return;
        }
        double balance = result.getAsDouble();
        p.sendMessage(Component.text(name + "'s is balance: " + balance).color(NamedTextColor.GREEN));

        cooldownManager.startCooldown(uuid);
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
