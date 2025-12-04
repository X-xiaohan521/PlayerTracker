package unimilk.playertracker.impl.v1_13.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import unimilk.playertracker.status.PlayerStatusManager;
import unimilk.playertracker.status.PlayingWith;
import unimilk.playertracker.api.util.ICatListener;
import unimilk.playertracker.log.ActivityLogger;

public class CatListenerImpl implements ICatListener {
    private final PlayerStatusManager playerStatusManager;
    private final ActivityLogger logger;

    public CatListenerImpl(PlayerStatusManager playerStatusManager, ActivityLogger logger) {
        this.playerStatusManager = playerStatusManager;
        this.logger = logger;
    }

    @EventHandler
    public void onPlayingWithPets(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (!playerStatusManager.isPlayingWith(player).equals(PlayingWith.NOTHING)) {
            return;
        }
        if (entity.getType().equals(EntityType.OCELOT)) {
            logger.log(player, "开始和 " + PlayingWith.CAT + " 玩");
            playerStatusManager.setPlaying(event.getPlayer(), PlayingWith.CAT, (Tameable)entity);
        } else if (entity.getType().equals(EntityType.WOLF)) {
            logger.log(player, "开始和 " + PlayingWith.DOG + " 玩");
            playerStatusManager.setPlaying(event.getPlayer(), PlayingWith.DOG, (Tameable)entity);
        } else if (entity.getType().equals(EntityType.PARROT)) {
            logger.log(player, "开始和 " + PlayingWith.PARROT + " 玩");
            playerStatusManager.setPlaying(event.getPlayer(), PlayingWith.PARROT, (Tameable)entity);
        }
    }

}
