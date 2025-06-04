package me.sirmonkeyboy.bank.Listeners;

import me.sirmonkeyboy.bank.Utils.MariaDB;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;

public class PlayerJoinListener implements Listener {

    private final MariaDB data;

    public PlayerJoinListener(MariaDB data){
        this.data = data;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws SQLException {
        Player p = e.getPlayer();
        data.createPlayer(p);
    }
}
