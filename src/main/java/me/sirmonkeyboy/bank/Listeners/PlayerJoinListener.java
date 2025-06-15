package me.sirmonkeyboy.bank.Listeners;

import me.sirmonkeyboy.bank.Utils.MariaDB;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    private final MariaDB data;

    public PlayerJoinListener(MariaDB data){
        this.data = data;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        data.createPlayer(player, (success) -> {
        });
    }
}
