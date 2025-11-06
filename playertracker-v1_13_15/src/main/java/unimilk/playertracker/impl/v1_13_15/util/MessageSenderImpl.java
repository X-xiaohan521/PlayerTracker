package unimilk.playertracker.impl.v1_13_15.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageSenderImpl {
    public void sendPlayerInfo(CommandSender sender, Player target) {
        sender.sendMessage("Here's a test message from 1.13~1.15.");
    }
}
