package unimilk.playertracker.util;

import org.bukkit.entity.Player;
import org.bukkit.Location;

import net.md_5.bungee.api.ChatColor;

public class DirectionDistanceCalc {
    
    // 计算追踪者到目标的方位
    public static String getDirection(Player tracker, Player target) {
        Location from = tracker.getLocation();
        Location to = target.getLocation();

        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double angle = Math.toDegrees(Math.atan2(-dx, dz)); // 注意 Minecraft Z轴为南北

        angle = (angle + 360) % 360;

        if (angle < 22.5 || angle >= 337.5) return "正南";
        if (angle < 67.5) return "西南";
        if (angle < 112.5) return "正西";
        if (angle < 157.5) return "西北";
        if (angle < 202.5) return "正北";
        if (angle < 247.5) return "东北";
        if (angle < 292.5) return "正东";
        return "东南";
    }

    // 计算追踪者和目标间的距离
    public static int getDistance(Player tracker, Player target) {
        return (int)tracker.getLocation().distance(target.getLocation());
    }
    
    // 生成BossBar信息函数
    public static String generateBossBarTitle(Player tracker, Player target) {
        return ChatColor.YELLOW + "玩家：" + ChatColor.GREEN + target.getName()
                + ChatColor.YELLOW  + " | 方向：" + ChatColor.WHITE + getDirection(tracker, target)
                + ChatColor.YELLOW + " | 距离：" + ChatColor.WHITE + getDistance(tracker, target);
    }
}
