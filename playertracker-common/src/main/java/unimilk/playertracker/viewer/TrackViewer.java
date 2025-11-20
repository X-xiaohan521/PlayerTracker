package unimilk.playertracker.viewer;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import unimilk.playertracker.status.PlayerStatusManager;
import net.md_5.bungee.api.ChatColor;

public class TrackViewer {
    // 追踪查看器类，用于实时跟踪目标状态，并显示在追踪者游戏画面上
    private final BossBarManager manager;
    private final PlayerStatusManager playerStatusManager;
    private Map<Player, Player> trackingMap = new HashMap<>(); // 用于存储追踪者和目标玩家之间的映射

    public TrackViewer(BossBarManager manager, PlayerStatusManager playerStatusManager) {
        // 构造方法，接收插件实例
        this.manager = manager; // 初始化BossBar管理器
        this.playerStatusManager = playerStatusManager;
    }

    public void addTracker(Player tracker, Player target) {
        // 添加追踪器方法
        trackingMap.put(tracker, target);
        manager.removeBossBar(tracker);
        manager.addBossBar(tracker, BossBarManager.generateBossBarTitle(tracker, target));
    }

    public Player removeTracker(Player tracker) {
        // 移除追踪器方法
        manager.removeBossBar(tracker);
        return trackingMap.remove(tracker);
    }

    public Player getTarget(Player tracker) {
        // 获取追踪关系方法
        return trackingMap.get(tracker);
    }

    public void removeAllTracker() {
        // 清空所有追踪器方法
        manager.removeAllBossBar();
        trackingMap.clear();
    }

    public void viewPlayerInfo(Player tracker, Player target) {
        // 将目标玩家信息显示在追踪者的游戏画面上

        // 获取信息
        String worldName = target.getWorld().getName(); // 获取目标玩家所在世界名称
        String coords = this.playerStatusManager.getCoords(target); // 获取目标玩家坐标
        String activity = this.playerStatusManager.getStatus(target); // 获取目标玩家当前活动状态

        // 构建信息
        // 主信息组件
        TextComponent message = new TextComponent(
            ChatColor.YELLOW + "正在追踪: " + ChatColor.GREEN + target.getName() +
            ChatColor.YELLOW + " - 世界: " + ChatColor.AQUA + worldName +
            ChatColor.YELLOW + " - 坐标: [" + ChatColor.AQUA + coords + 
            ChatColor.YELLOW + "] - 当前状态: " + ChatColor.LIGHT_PURPLE + activity);
        
            // 发送到追踪者的游戏画面上
        tracker.spigot().sendMessage(ChatMessageType.ACTION_BAR, message);
    }

    public void refreshTracker(Player player) {
        // 更新追踪状态方法
        for (Map.Entry<Player, Player> entry : trackingMap.entrySet()) {
            Player tracker = entry.getKey(); // 获取追踪者
            Player target = entry.getValue(); // 获取目标玩家
            if (player.equals(tracker) || player.equals(target)) {
                // 如果移动者是目标或追踪者之一，更新追踪器
                if (tracker.isOnline() && target.isOnline()) {
                    // 如果追踪者和目标玩家都在线，发送目标玩家信息
                    viewPlayerInfo(tracker, target);
                    manager.updateBossBar(tracker, BossBarManager.generateBossBarTitle(tracker, target));
                    manager.showBossBar(tracker);
                }
                else if (tracker.isOnline()) {
                    // 如果目标玩家不在线，显示错误消息
                    tracker.spigot().sendMessage(
                        ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.YELLOW + "目标玩家 " + ChatColor.GREEN + target.getName() + ChatColor.RED + " 不在线。"));
                    manager.hideBossBar(tracker);
                }
                else {
                    continue; // 如果追踪者不在线，跳过
                }
            }
        }
    }
}