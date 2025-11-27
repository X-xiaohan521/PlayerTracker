package unimilk.playertracker.impl.v1_14_15.util;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import unimilk.playertracker.api.util.ICatListener;
import unimilk.playertracker.log.ActivityLogger;
import unimilk.playertracker.status.PlayerStatusManager;
import unimilk.playertracker.status.PlayingWith;

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
        if (entity.getType().equals(EntityType.CAT)) {
            playerStatusManager.setPlaying(event.getPlayer(), PlayingWith.CAT, (Tameable)entity);
            logger.log(player, "开始和小猫玩");
        } else if (entity.getType().equals(EntityType.WOLF)) {
            playerStatusManager.setPlaying(event.getPlayer(), PlayingWith.DOG, (Tameable)entity);
            logger.log(player, "开始和小狗玩");
        } else if (entity.getType().equals(EntityType.PARROT)) {
            playerStatusManager.setPlaying(event.getPlayer(), PlayingWith.PARROT, (Tameable)entity);
            logger.log(player, "开始和小鹦鹉玩");
        }
    }
    
}
