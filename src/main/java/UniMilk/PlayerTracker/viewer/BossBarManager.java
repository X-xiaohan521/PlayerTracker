package unimilk.playertracker.viewer;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.boss.*;
import org.bukkit.entity.Player;

public class BossBarManager {
    // BossBar管理器类
    private final Map<Player, BossBar> bars = new HashMap<>(); // 定义玩家和BossBar的映射Map

    public void addBossBar(Player player, String title) {
        // 添加BossBar函数
        BossBar bar = Bukkit.createBossBar(title, BarColor.PURPLE, BarStyle.SOLID); // 创建BossBar
        bar.addPlayer(player); // 将BossBar分配给玩家
        bar.setVisible(true); // 设置BossBar为可见
        bars.put(player, bar); // 将映射关系添加进Bars中
    }

    public void updateBossBar(Player player, String newTitle) {
        // 更新BossBar函数
        BossBar bar = bars.get(player); // 获取玩家对应的BossBar
        if (bar != null) bar.setTitle(newTitle); // 刷新BossBar上的Title
    }

    public void showBossBar(Player player) {
        // 显示BossBar函数
        BossBar bar = bars.get(player); // 设置BossBar为可见
        bar.setVisible(true); // 设置BossBar为可见
    }
    
    public void hideBossBar(Player player) {
        // 隐藏BossBar函数
        BossBar bar = bars.get(player); // 设置BossBar为可见
        bar.setVisible(false); // 设置BossBar为不可见
    }

    public void removeBossBar(Player player) {
        // 删除BossBar函数
        BossBar bar = bars.remove(player); // 移除玩家和BossBar映射关系
        if (bar != null) {
            bar.setVisible(false); // 设置BossBar为不可见
            bar.removeAll(); // 移除BossBar，释放内存
        }
    }
}
