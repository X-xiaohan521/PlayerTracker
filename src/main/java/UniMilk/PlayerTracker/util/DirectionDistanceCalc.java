package unimilk.playertracker.util;

import org.bukkit.entity.Player;
import org.bukkit.Location;

import net.md_5.bungee.api.ChatColor;

public class DirectionDistanceCalc {
    
    public static String getHorizontalDirection(Player tracker, Player target) {
        // 计算追踪者（A）到目标（B）的水平方位
        Location from = tracker.getLocation();
        Location to = target.getLocation();

        double yaw = from.getYaw();

        double dx = to.getX() - from.getX();
        double dz = to.getZ() - from.getZ();
        double angle = Math.toDegrees(Math.atan2(-dx, dz)); // 计算直线AB的倾斜角（注意 Minecraft Z轴为南北，故dx前添负号）

        angle = (angle + 360) % 360; // 计算AB与正南方向的夹角（转换为0~360°）
        double theta = angle - yaw; // 减去A视线朝向角度，所得即为A的视线与AB方向的夹角

        if (theta < 22.5 || theta >= 337.5) return "↑";
        if (theta < 67.5) return "↗";
        if (theta < 112.5) return "→";
        if (theta < 157.5) return "↘";
        if (theta < 202.5) return "↓";
        if (theta < 247.5) return "↙";
        if (theta < 292.5) return "←";
        return "↖";
    }

    public static String getVerticalDirection(Player tracker, Player target) {
        // 计算A到B的垂直方位
        Location from = tracker.getLocation();
        Location to = target.getLocation();

        if (from.getY() < to.getY()) {
            return "↑";
        } else if (from.getY() > to.getY()) {
            return "↓";
        } else {
            return "水平";
        }
    }
    
    public static int getDistance(Player tracker, Player target) {
        // 计算追踪者和目标间的距离
        return (int)tracker.getLocation().distance(target.getLocation());
    }
    
    public static String generateBossBarTitle(Player tracker, Player target) {
        // 生成BossBar信息函数
        return ChatColor.YELLOW + "玩家：" + ChatColor.GREEN + target.getName()
                + ChatColor.YELLOW  + " | 水平方向：" + ChatColor.WHITE + getHorizontalDirection(tracker, target)
                + ChatColor.YELLOW + " | 垂直方向" + ChatColor.WHITE + getVerticalDirection(tracker, target)
                + ChatColor.YELLOW + " | 距离：" + ChatColor.WHITE + getDistance(tracker, target);
    }
}
