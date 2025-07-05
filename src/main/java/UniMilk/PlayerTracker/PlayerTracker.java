package unimilk.playertracker;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import unimilk.playertracker.command.CommandHandler;
import unimilk.playertracker.log.*;
import unimilk.playertracker.viewer.TrackViewer;

public class PlayerTracker extends JavaPlugin {

    public FileConfiguration config; // 初始化配置文件对象
    private ActivityLogger logger; // 初始化活动记录器对象
    public TrackViewer viewer; // 初始化追踪器对象
    private CommandHandler commandHandler; // 初始化命令处理器对象

    @Override
    public void onEnable() {
        // 插件加载时的初始化逻辑
        getLogger().info("PlayerTracker 插件正在加载...");
        
        // 加载配置文件
        loadConfig();
        
        // 加载活动记录器
        logger = new ActivityLogger(this); // 创建活动记录器实例
        logger.scheduleLogging(); // 启动定时记录任务
        getServer().getPluginManager().registerEvents(new EventListener(logger), this); // 注册事件监听器
        
        // 加载追踪器
        viewer = new TrackViewer(this); // 创建跟踪器实例
        viewer.startTrackingLoop(); // 启动跟踪循环

        // 加载命令处理器
        commandHandler = new CommandHandler(this); // 创建命令处理器实例
        this.getCommand("playertracker").setExecutor(commandHandler); // 注册命令处理器

        getLogger().info("PlayerTracker 插件加载完毕！");
    }

    private void loadConfig() {
        // 读取配置文件函数
        saveDefaultConfig(); // 如果配置文件不存在，则创建默认配置文件
        config = getConfig(); // 获取配置文件对象

        // 检查插件是否启用
        if (config.getBoolean("plugin.enabled", true)) {
            getLogger().info("PlayerTracker 插件已启用！");
        } else {
            getLogger().warning("PlayerTracker 插件已禁用，请检查配置文件！");
        }
    }

    public void onConfigReload() {
        // 重载配置文件函数

        // 更新 config 对象
        this.config = getConfig();

        // 重新启动定时任务
        Bukkit.getScheduler().cancelTasks(this); // 停止所有任务
        logger.scheduleLogging(); // 启动定时记录任务
        viewer.startTrackingLoop(); // 启动跟踪循环

        getLogger().info("配置文件已重新加载！");
    }

}