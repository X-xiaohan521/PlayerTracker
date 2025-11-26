package unimilk.playertracker.impl.v1_13.util;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import unimilk.playertracker.api.util.IMessageSender;
import unimilk.playertracker.api.util.IPlayerStatusManager;

public class MessageSenderImpl implements IMessageSender {
    private final IPlayerStatusManager playerStatusManager;

    private MessageSenderImpl(IPlayerStatusManager playerStatusManager) {
        this.playerStatusManager = playerStatusManager;
    }

    public static IMessageSender newMessageSender(IPlayerStatusManager playerStatusManager) {
        return new MessageSenderImpl(playerStatusManager);
    }

    public void sendPlayerInfo(CommandSender sender, Player player) {
        // 发送玩家信息函数
        Location loc = player.getLocation();
        String activity = this.playerStatusManager.getStatus(player);
        String worldName = loc.getWorld().getName();
        String coords = this.playerStatusManager.getCoords(player);
        
        // 主信息组件（旧版本不支持鼠标悬停和点击事件，以 plain text 显示）
        TextComponent message = new TextComponent(
            ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " - 世界: " + ChatColor.AQUA + worldName +
            ChatColor.YELLOW + " - 坐标: [" + ChatColor.AQUA + coords + ChatColor.YELLOW + "] - 当前状态: " + ChatColor.LIGHT_PURPLE + activity);

        // 发送
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(message);
        } else {
            // 控制台无法显示富文本，输出纯文本版本
            sender.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " - 世界: " + ChatColor.AQUA + worldName + ChatColor.YELLOW + " - 坐标: [" + ChatColor.AQUA + coords + ChatColor.YELLOW + "] - 当前状态: " + ChatColor.LIGHT_PURPLE + activity);
        }
    }
}
