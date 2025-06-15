package me.sirmonkeyboy.bank.Commands;

import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.List;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(Player player, String args[]) throws SQLException;

    public abstract List<String> getSubCommandArguments(Player p, String args[]);

}