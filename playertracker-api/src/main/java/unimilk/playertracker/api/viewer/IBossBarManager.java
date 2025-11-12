package unimilk.playertracker.api.viewer;

import org.bukkit.entity.Player;

public interface IBossBarManager {
    public String generateBossBarTitle(Player tracker, Player target);
}