package unimilk.playertracker.util;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import unimilk.playertracker.api.util.IPlayerStatusUtils;

public class PlayerStatusUtils {
    private static IPlayerStatusUtils statusUtils;
    
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
        // 调用 sendPlayerInfo 接口
        statusUtils.sendPlayerInfo(sender, player);
    }
}
