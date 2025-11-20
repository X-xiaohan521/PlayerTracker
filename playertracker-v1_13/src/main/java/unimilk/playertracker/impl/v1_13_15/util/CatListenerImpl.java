package unimilk.playertracker.impl.v1_13_15.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import unimilk.playertracker.status.PlayerStatusManager;
import unimilk.playertracker.status.PlayingWith;
import unimilk.playertracker.api.util.ICatListener;

public class CatListenerImpl implements ICatListener {
    private final PlayerStatusManager playerStatusManager;

    public CatListenerImpl(PlayerStatusManager playerStatusManager) {
        this.playerStatusManager = playerStatusManager;
    }

    @EventHandler
    public void onPlayingWithPets(PlayerInteractEntityEvent event) {
        Entity entity = event.getRightClicked();
        if (entity.getType().equals(EntityType.OCELOT)) {
            playerStatusManager.setPlaying(event.getPlayer(), PlayingWith.CAT);
        } else if (entity.getType().equals(EntityType.WOLF)) {
            playerStatusManager.setPlaying(event.getPlayer(), PlayingWith.DOG);
        } else if (entity.getType().equals(EntityType.PARROT)) {
            playerStatusManager.setPlaying(event.getPlayer(), PlayingWith.PARROT);
        }
    }

    
}
