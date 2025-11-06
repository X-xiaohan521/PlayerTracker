package unimilk.playertracker.impl.v1_16_21.util;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import unimilk.playertracker.util.PlayerStatusUtils;

public class MessageSenderImpl {
    public MessageSenderImpl() {

    }

    public void sendPlayerInfo(CommandSender sender, Player player) {
        // 发送玩家信息函数
        Location loc = player.getLocation();
        String activity = PlayerStatusUtils.getStatus(player);
        String worldName = loc.getWorld().getName();
        String coords = PlayerStatusUtils.getCoords(player);
        
        // 主信息组件
        TextComponent message = new TextComponent(
            ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " - 世界: " + ChatColor.AQUA + worldName + ChatColor.YELLOW + " - 坐标: [");

        // 可点击的坐标组件
        TextComponent coordComponent = new TextComponent(ChatColor.AQUA + coords);
        coordComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, coords));
        coordComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("点击复制坐标")));

        // 当前状态信息
        TextComponent statusComponent = new TextComponent(ChatColor.YELLOW + "] - 当前状态: " + ChatColor.LIGHT_PURPLE + activity);

        // 拼接
        message.addExtra(coordComponent);
        message.addExtra(statusComponent);

        // 发送（必须是 Player 才能看到富文本）
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(message);
        } else {
            // 控制台无法显示富文本，输出纯文本版本
            sender.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " - 世界: " + ChatColor.AQUA + worldName + ChatColor.YELLOW + " - 坐标: [" + ChatColor.AQUA + coords + ChatColor.YELLOW + "] - 当前状态: " + ChatColor.LIGHT_PURPLE + activity);
        }
    }
}