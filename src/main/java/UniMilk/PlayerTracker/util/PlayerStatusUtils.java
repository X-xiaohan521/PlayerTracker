package unimilk.playertracker.util;

import unimilk.playertracker.*;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;

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

    public static void sendPlayerInfo(CommandSender sender, Player player, PlayerTracker plugin) {
        // 发送玩家信息函数
        Location loc = player.getLocation();
        String activity = getStatus(player);
        String worldName = loc.getWorld().getName();
        String coords = getCoords(player);
        
        // 主信息组件
        TextComponent message = Component.text(player.getName(), NamedTextColor.GREEN)
            .append(Component.text(" - 世界: ").color(NamedTextColor.YELLOW))
            .append(Component.text(worldName).color(NamedTextColor.AQUA))
            .append(Component.text(" - 坐标: [").color(NamedTextColor.YELLOW))
            .append(Component.text(coords).color(NamedTextColor.AQUA)
                .hoverEvent(HoverEvent.showText(Component.text("点击复制坐标")))
                .clickEvent(ClickEvent.copyToClipboard(coords)))
            .append(Component.text("] - 当前状态: ").color(NamedTextColor.YELLOW))
            .append(Component.text(activity).color(NamedTextColor.LIGHT_PURPLE));

        // 发送（必须是 Player 才能看到富文本）       
        plugin.adventure().sender(sender).sendMessage(message);

    }
}
