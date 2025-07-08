package unimilk.playertracker.viewer;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

import unimilk.playertracker.PlayerTracker;

public class DirectionViewer {
    private PlayerTracker plugin = new PlayerTracker();
    private Map<Player, BossBar> barMap = new HashMap<>();
    
    public DirectionViewer(PlayerTracker plugin) {
        this.plugin = plugin;
    }
    
    // 计算追踪者到目标的方位
    private String getDirection(Player tracker, Player target) {
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

    
    // 给每个玩家创建对应的BossBar
    public void createBar() {
        for (Entry<Player, Player> entry : plugin.viewer.getTrackingMap().entrySet()) {
            Player tracker = entry.getKey();
            Player target = entry.getValue();

        }
    }

    // 定时刷新BossBar
    public void scheduledRefreshBar() {
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Entry<Player, BossBar> entry : barMap.entrySet()) {
                Player tracker = entry.getKey();
                BossBar bar = entry.getValue();

                bar.setTitle(ChatColor.YELLOW + "玩家：");
            }
        }, 0L, 20L);
    }
}
