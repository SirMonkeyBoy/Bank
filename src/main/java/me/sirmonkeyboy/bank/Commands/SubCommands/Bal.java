package me.sirmonkeyboy.bank.Commands.SubCommands;

import me.sirmonkeyboy.bank.Bank;
import me.sirmonkeyboy.bank.Commands.SubCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;


public class Bal extends SubCommand {

    private final Bank plugin;

    public Bal(Bank plugin) {
        this.plugin = plugin;
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
    public void perform(Player p, String[] args) throws SQLException {
        if (p.hasPermission("Bank.commands.Bank.Balance")) {
            double balance = plugin.data.getBalance(p.getUniqueId());
            String BalanceMessage = plugin.getConfig().getString("BalanceMessage");
            if (BalanceMessage != null) {
                String BalanceStr = String.valueOf(balance);
                BalanceMessage = BalanceMessage.replace("%Bal%", BalanceStr);
                p.sendMessage(Component.text(BalanceMessage).color(NamedTextColor.GREEN));
            }
        } else {
            if (!p.hasPermission("Bank.commands.Bank.Balance")) {
                String noPermission = plugin.getConfig().getString("NoPermission");
                if (noPermission != null) {
                    p.sendMessage(Component.text(noPermission).color(NamedTextColor.RED));
                }
            }
        }
    }

    @Override
    public List<String> getSubCommandArguments(Player p, String[] args) {
        return null;
    }
}
