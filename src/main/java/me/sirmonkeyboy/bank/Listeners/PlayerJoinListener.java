package me.sirmonkeyboy.bank.Listeners;

import me.sirmonkeyboy.bank.Bank;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {

    private final Bank plugin;

    public PlayerJoinListener(Bank plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player p = e.getPlayer();
        plugin.data.createPlayer(p);
    }
}
