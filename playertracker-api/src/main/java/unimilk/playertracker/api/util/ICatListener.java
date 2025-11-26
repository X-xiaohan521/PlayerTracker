package unimilk.playertracker.api.util;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public interface ICatListener extends Listener {
    @EventHandler
    public void onPlayingWithPets(PlayerInteractEntityEvent event);
}
