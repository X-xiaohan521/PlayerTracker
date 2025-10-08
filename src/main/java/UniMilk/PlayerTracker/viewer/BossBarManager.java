package unimilk.playertracker.viewer;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;

import unimilk.playertracker.util.DirectionDistanceCalc;

public class BossBarManager {
    // BossBar管理器类
    private final Map<Player, BossBar> bars = new HashMap<>(); // 定义玩家和BossBar的映射Map

    public void addBossBar(Player player, Component title) {
        // 添加BossBar函数
        BossBar bar = BossBar.bossBar(title, 1, Color.PURPLE, BossBar.Overlay.NOTCHED_20); // 创建BossBar
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

    public void removeAllBossBar() {
        // 清空BossBar函数
        for (BossBar bar : bars.values()) bar.removeAll();
        bars.clear();
    }

    public static TextComponent generateBossBarTitle(Player tracker, Player target) {
        // 生成BossBar信息函数
        TextComponent message = Component.text("玩家：", NamedTextColor.YELLOW)
            .append(Component.text(target.getName()).color(NamedTextColor.GREEN))
            .append(Component.text(" | 水平方向：").color(NamedTextColor.YELLOW))
            .append(Component.text(DirectionDistanceCalc.getHorizontalDirection(tracker, target)).color(NamedTextColor.WHITE))
            .append(Component.text(" | 垂直方向：").color(NamedTextColor.YELLOW))
            .append(Component.text(DirectionDistanceCalc.getVerticalDirection(tracker, target)).color(NamedTextColor.WHITE))
            .append(Component.text(" | 距离：").color(NamedTextColor.YELLOW))
            .append(Component.text(DirectionDistanceCalc.getDistance(tracker, target)).color(NamedTextColor.WHITE));
        return message;
    }
}
