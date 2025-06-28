package unimilk.playertracker.viewer;

import unimilk.playertracker.util.PlayerStatusUtils;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.ChatColor;

public class TrackViewer {
    // 追踪查看器类，用于实时跟踪目标状态，并显示在追踪者游戏画面上
    private final JavaPlugin plugin;
    private Map<Player, Player> trackingMap = new HashMap<>(); // 用于存储追踪者和目标玩家之间的映射

    public TrackViewer(JavaPlugin plugin) {
        // 构造函数，接收插件实例
        this.plugin = plugin; // 初始化插件实例
    }

    public void addTracker(Player tracker, Player target) {
        // 添加追踪器函数
        trackingMap.put(tracker, target);
    }

    public Player removeTracker(Player tracker) {
        // 移除追踪器函数
        return trackingMap.remove(tracker);
    }

    public static void viewPlayerInfo(Player tracker, Player target) {
        // 将目标玩家信息显示在追踪者的游戏画面上

        // 获取信息
        String worldName = target.getWorld().getName(); // 获取目标玩家所在世界名称
        String coords = PlayerStatusUtils.getCoords(target); // 获取目标玩家坐标
        String activity = PlayerStatusUtils.getStatus(target); // 获取目标玩家当前活动状态
        
        // 发送到追踪者的游戏画面上
        tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
            ChatColor.YELLOW + "正在追踪: " + ChatColor.GREEN + target.getName() +
            ChatColor.YELLOW + " - 世界: " + ChatColor.AQUA + worldName +
            ChatColor.YELLOW + " - 坐标: [" + ChatColor.AQUA + coords +
            ChatColor.YELLOW + "] - 当前状态: " + ChatColor.LIGHT_PURPLE + activity
        ));
    }

    public void startTrackingLoop() {
        // 定时任务，每秒更新一次追踪状态
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Map.Entry<Player, Player> entry : trackingMap.entrySet()) {
                Player tracker = entry.getKey(); // 获取追踪者
                Player target = entry.getValue(); // 获取目标玩家

                if (tracker.isOnline() && target.isOnline()) {
                    // 如果追踪者和目标玩家都在线，发送目标玩家信息
                    viewPlayerInfo(tracker, target);
                }
                else if (tracker.isOnline()) {
                    // 如果目标玩家不在线，显示错误消息
                    tracker.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "目标玩家 " + ChatColor.GREEN + target.getName() + ChatColor.RED + " 不在线。"));
                }
                else {
                    continue; // 如果追踪者不在线，跳过
                }
            }
        }, 0L, 20L);
    }
}