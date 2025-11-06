package unimilk.playertracker.api.util;

import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

public interface IPlayerStatusUtils {
    public void sendPlayerInfo(CommandSender sender, Player player);
}
