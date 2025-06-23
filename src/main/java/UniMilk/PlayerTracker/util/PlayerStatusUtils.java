package unimilk.playertracker.util;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

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

    public static void sendPlayerInfo(CommandSender sender, Player player) {
        // 发送玩家信息函数
        Location loc = player.getLocation();
        String activity = getStatus(player);

        sender.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW +
                " - 坐标: [" + ChatColor.AQUA + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ChatColor.YELLOW + "]" +
                " - 当前状态: " + ChatColor.LIGHT_PURPLE + activity);
    }
}
