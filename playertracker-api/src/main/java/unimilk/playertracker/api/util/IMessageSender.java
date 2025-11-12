package unimilk.playertracker.api.util;

import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

public interface IMessageSender {
    public void sendPlayerInfo(CommandSender sender, Player player);
}
