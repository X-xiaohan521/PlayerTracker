package unimilk.playertracker;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import net.kyori.adventure.platform.bukkit.BukkitAudiences;

import unimilk.playertracker.command.CommandHandler;
import unimilk.playertracker.command.CommandTabCompleter;
import unimilk.playertracker.log.*;
import unimilk.playertracker.util.EventListener;
import unimilk.playertracker.viewer.BossBarManager;
import unimilk.playertracker.viewer.TrackViewer;

public class PlayerTracker extends JavaPlugin {

    private boolean isEnabled; // 初始化插件启用状态标识
    private FileConfiguration config; // 初始化配置文件对象
    private String version; // 初始化游戏版本变量
    private ActivityLogger logger; // 初始化活动记录器对象
    private TrackViewer viewer; // 初始化追踪器对象
    private CommandHandler commandHandler; // 初始化命令处理器对象
    private CommandTabCompleter commandTabCompleter; // 初始化Tab补全器对象
    private BossBarManager manager; // 初始化BossBar管理器
    private BukkitAudiences adventure; // 初始化Audience对象，用于处理插件对玩家发送的消息

    @Override
    // 插件加载时的初始化逻辑
    public void onEnable() {
        getLogger().info("PlayerTracker 插件正在加载...");
        
        // 加载配置文件
        isEnabled = loadConfig();
        
        // 读取游戏版本
        version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

        // 加载活动记录器
        logger = new ActivityLogger(this); // 创建活动记录器实例
        
        // 加载BossBar管理器
        manager = new BossBarManager(); // 创建BossBar管理器实例
        
        // 加载追踪器
        viewer = new TrackViewer(manager); // 创建跟踪器实例
        
        // 加载命令处理器
        commandHandler = new CommandHandler(this, viewer); // 创建命令处理器实例
        this.getCommand("playertracker").setExecutor(commandHandler); // 注册命令处理器

        // 加载 Tab补全器
        commandTabCompleter = new CommandTabCompleter(this); // 创建Tab补全器实例
        this.getCommand("playertracker").setTabCompleter(commandTabCompleter); // 注册Tab补全器

        // 加载消息处理器
        this.adventure = BukkitAudiences.create(this);

        if (isEnabled) {
            // 如果插件启用，则启动组件
            logger.scheduleLogging(); // 启动定时记录任务
            getServer().getPluginManager().registerEvents(new EventListener(logger, viewer), this); // 注册事件监听器
        }

        getLogger().info("PlayerTracker 插件加载完毕！状态：" + (isEnabled ? "已启用" : "已禁用"));
    }

    // 插件配置相关函数
    private boolean loadConfig() {
        // 读取配置文件函数
        saveDefaultConfig(); // 如果配置文件不存在，则创建默认配置文件
        config = getConfig(); // 获取配置文件对象

        // 检查插件是否启用
        if (config.getBoolean("plugin.enabled", true)) {
            getLogger().info("PlayerTracker 插件已启用！");
            return true;
        } else {
            getLogger().warning("PlayerTracker 插件已禁用，请检查配置文件！");
            return false;
        }
    }

    public void setConfig(String key, Object value) {
        // 设置配置项函数
        config.set(key, value);
        this.saveConfig();
    }

    public boolean getConfigBoolean(String key) {
        // 获取配置项布尔值函数
        return config.getBoolean(key);
    }

    public int getConfigInt(String key) {
        // 获取配置项整型值函数
        return config.getInt(key);
    }

    public boolean isPluginEnabled() {
        // 获取插件启用状态函数
        return isEnabled;
    }

    public String getGameVersion() {
        // 获取游戏版本方法
        return version;
    }

    public BukkitAudiences adventure() {
        // 获取消息处理器方法
        if (this.adventure == null) {
            throw new IllegalStateException("Tried to access Adventure while disabled!");
        }
        return this.adventure;
    }

    public void onConfigReload() {
        // 重载插件函数
        // 注销所有组件
        Bukkit.getScheduler().cancelTasks(this); // 停止所有定时任务
        viewer.removeAllTracker(); // 清除追踪器
        HandlerList.unregisterAll(this); // 注销所有监听器
        
        // 更新 config 对象
        this.reloadConfig();
        this.config = getConfig();

        // 重新判断插件启用情况
        isEnabled = config.getBoolean("plugin.enabled");
        if (isEnabled) {
            logger.scheduleLogging(); // 重新启动定时记录任务
            getServer().getPluginManager().registerEvents(new EventListener(logger, viewer), this); // 重新注册事件监听器
        }

        getLogger().info("PlayerTracker 插件已重新加载！状态：" + (isEnabled ? "已启用" : "已禁用"));
    }

    @Override
    public void onDisable() {
        // 注销消息处理器
        if (this.adventure != null) {
            this.adventure.close();
            this.adventure = null;
        }
    }

}