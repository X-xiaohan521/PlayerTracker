package unimilk.playertracker.log;

import java.io.*;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import unimilk.playertracker.util.PlayerStatusUtils;

public class ActivityLogger {
    private final JavaPlugin plugin; // 定义插件实例
    private final File logFile; // 定义日志文件实例

    public ActivityLogger(JavaPlugin plugin) {
        // 构造函数，接收插件实例
        this.plugin = plugin; // 初始化插件实例
        this.logFile = new File(plugin.getDataFolder(), "player_activities.log"); // 日志文件路径
    }

    public void log(Player player, String activity) {
        // 日志记录函数
        if (!plugin.getConfig().getBoolean("log.enabled")) return; // 如果日志记录未启用，则返回
        
        // 获取当前时间并格式化
        String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

        // 构建日志（格式：时间 玩家名 位置【世界+坐标】 事件）
        String worldName = player.getWorld().getName();
        String coords = PlayerStatusUtils.getCoords(player); // 获取玩家坐标
        String logMessage = String.format("[%s] [%s] [%s] [%s] [%s]", timeStamp, player.getName(), worldName, coords, activity); // 日志格式

        // 使用 try-catch 块处理 IOException 异常
        try (FileWriter writer = new FileWriter(logFile, true)) { // 使用 try-with-resources 自动关闭资源
            writer.write(logMessage + "\n"); // 写入日志
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "无法写入日志文件！", e); // 捕获并记录错误
        }
    }

    public void scheduleLogging() {
        // 定时记录玩家活动函数
        int logInterval = plugin.getConfig().getInt("log.schedule", 300); // 获取日志记录间隔，默认为300秒
        plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String activity = PlayerStatusUtils.getStatus(player); // 获取玩家活动状态
                log(player, activity); // 记录日志
            }
        }, 0L, logInterval * 20L); // 每隔指定时间记录一次活动
    }

    /*
    private void scheduleLogCleanup() {
        // 定时清理日志文件函数
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            File logFile = new File(getDataFolder(), "player_activities.log"); // 日志文件路径
            if (logFile.exists()) {
                try {
                    FileWriter writer = new FileWriter(logFile, false); // 清空文件
                    writer.close(); // 关闭文件写入器
                } catch (IOException e) {
                    getLogger().log(Level.SEVERE, "无法清理日志文件！", e);
                }
            }
        }, 0L, 72000L); // 每3600秒（1小时）清理一次
    }
     */
}
