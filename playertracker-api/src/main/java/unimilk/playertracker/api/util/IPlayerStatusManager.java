package unimilk.playertracker.api.util;

import org.bukkit.entity.Player;

public interface IPlayerStatusManager {
    public String getStatus(Player player);
    public String getCoords(Player player);
}
