package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Commands.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public class Help extends SubCommand {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "List of /bank subcommands";
    }

    @Override
    public String getSyntax() {
        return "/bank help";
    }

    @Override
    public void perform(Player p, String[] args) throws SQLException {
            p.sendMessage(Component.text("Bank usages"));
            p.sendMessage(Component.text("/bank balance or /bank bal - ").append(Component.text("Gets your bank balance").color(NamedTextColor.GOLD)));
            p.sendMessage(Component.text("/bank deposit (Amount) - ").append(Component.text("Deposits (Amount) into your account").color(NamedTextColor.GOLD)));
            p.sendMessage(Component.text("/bank withdraw (Amount) - ").append(Component.text("Withdraws (Amount) from your account").color(NamedTextColor.GOLD)));
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return List.of();
    }
}
