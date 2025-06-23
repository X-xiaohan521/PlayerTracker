package unimilk.playertracker.util;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class PlayerStatusUtils {
    
    public static String getStatus(Player player) {
        // 获取玩家当前活动状态函数
        if (player.isSleeping()) return "睡觉中";
        if (player.isInsideVehicle()) return "乘坐载具";
        if (player.isSwimming()) return "游泳";
        if (player.isGliding()) return "滑翔中";
        if (player.isFlying()) return "飞行中";
        if (player.isSprinting()) return "疾跑";
        if (player.isSneaking()) return "潜行";
        return "闲逛";
    }

    public static String getCoords(Player player) {
        // 获取玩家坐标函数
        Location loc = player.getLocation();
        int x = loc.getBlockX(); // 获取玩家所在世界的X坐标
        int y = loc.getBlockY(); // 获取玩家所在世界的Y坐标
        int z = loc.getBlockZ(); // 获取玩家所在世界的Z坐标
        return String.format("%s %s %s", x, y, z);
    }

    public static void sendPlayerInfo(CommandSender sender, Player player) {
        // 发送玩家信息函数
        Location loc = player.getLocation();
        String activity = getStatus(player);
        String worldName = loc.getWorld().getName();
        String coords = getCoords(player);
        
        // 主信息组件
        TextComponent message = new TextComponent(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " - 世界: " + ChatColor.AQUA + worldName + ChatColor.YELLOW + " - 坐标: ");

        // 可点击的坐标组件
        TextComponent coordComponent = new TextComponent(ChatColor.AQUA + "[" + coords + "]");
        coordComponent.setClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, coords));
        coordComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("点击复制坐标")));

        // 当前状态信息
        TextComponent statusComponent = new TextComponent(ChatColor.YELLOW + " - 当前状态: " + ChatColor.LIGHT_PURPLE + activity);

        // 拼接
        message.addExtra(coordComponent);
        message.addExtra(statusComponent);

        // 发送（必须是 Player 才能看到富文本）
        if (sender instanceof Player) {
            ((Player) sender).spigot().sendMessage(message);
        } else {
            // 控制台无法显示富文本，输出纯文本版本
            sender.sendMessage(player.getName() + " - 坐标: [" + coords + "] - 当前状态: " + activity);
        }
    }
}
