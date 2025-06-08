package me.sirmonkeyboy.bank.Commands.ABankSubCommands;

import me.sirmonkeyboy.bank.Commands.SubCommand;
import me.sirmonkeyboy.bank.Utils.ConfigManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.util.List;

public class Reload extends SubCommand {

    private final ConfigManager configManager;

    public Reload(ConfigManager configManager) {
        this.configManager = configManager;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "reloads KingdomBank's config";
    }

    @Override
    public String getSyntax() {
        return "/abank reload";
    }

    @Override
    public void perform(Player p, String[] args) {
        if (!(p.hasPermission("Bank.commands.ABank.Reload"))) {
            p.sendMessage(Component.text(configManager.getNoPermission()).color(NamedTextColor.RED));
            return;
        }

        configManager.reloadConfigManager(p);
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return List.of();
    }
}
