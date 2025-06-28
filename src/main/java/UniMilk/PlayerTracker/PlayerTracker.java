package unimilk.playertracker;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

import unimilk.playertracker.log.*;
import unimilk.playertracker.listener.EventListener;
import unimilk.playertracker.util.PlayerStatusUtils;
import unimilk.playertracker.viewer.TrackViewer;

public class PlayerTracker extends JavaPlugin {

    private FileConfiguration config; // 初始化配置文件对象
    private ActivityLogger logger; // 初始化活动记录器对象
    private TrackViewer viewer; // 初始化追踪器对象

    @Override
    public void onEnable() {
        // 插件加载时的初始化逻辑
        getLogger().info("PlayerTracker 插件正在加载...");
        
        loadConfig(); // 加载配置文件
        
        logger = new ActivityLogger(this); // 创建活动记录器实例
        logger.scheduleLogging(); // 启动定时记录任务

        getServer().getPluginManager().registerEvents(new EventListener(logger), this); // 注册事件监听器
        
        viewer = new TrackViewer(this); // 创建跟踪器实例
        viewer.startTrackingLoop(); // 启动跟踪循环

        getLogger().info("PlayerTracker 插件加载完毕！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 处理 `/track` 命令
        if (command.getName().equalsIgnoreCase("track")) {
            // 检查是否有权限使用 /track 命令
            if (!sender.hasPermission("playertracker.use") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
                return true;
            }
            
            // 如果有权限，继续执行命令
            if (args.length == 0) {
                // 没有参数时，查询所有在线玩家
                sender.sendMessage(ChatColor.GOLD + "===== 在线玩家信息 =====");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    PlayerStatusUtils.sendPlayerInfo(sender, player);
                }
            } else {
                // 有参数时，查询特定玩家
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "玩家 " + args[0] + " 不在线或不存在！");
                } else {
                    PlayerStatusUtils.sendPlayerInfo(sender, target);
                }
            }
            return true;
        }
        
        // 处理 `/tracklog` 命令
        if (command.getName().equalsIgnoreCase("tracklog")) {
            // 检查是否有权限使用 /tracklog 命令
            if (!sender.hasPermission("playertracker.admin") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
                return true;
            }

            // 如果没有参数，显示当前日志记录状态
            if (args.length == 0) {
                sender.sendMessage( "日志记录状态： " + (config.getBoolean("log.enabled") ? ChatColor.GREEN + "已启用" : ChatColor.RED + "已禁用"));
                return true;
            }
            
            // 如果有参数，设置日志记录状态，并保存到配置文件
            if (args[0].equalsIgnoreCase("on")) {
                config.set("log.enabled", true);
                saveConfig();
                sender.sendMessage(ChatColor.GREEN + "日志记录已启用！");
            } else if (args[0].equalsIgnoreCase("off")) {
                config.set("log.enabled", false);
                saveConfig();
                sender.sendMessage(ChatColor.RED + "日志记录已禁用！");
            } else {
                sender.sendMessage(ChatColor.RED + "用法: /tracklog <on|off>");
            }
            return true;
        }

        // 处理 `/trackview` 命令
        if (command.getName().equalsIgnoreCase("trackview")) {
            // 控制台不能使用 /trackview 命令
            if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
                sender.sendMessage(ChatColor.RED + "该指令只能由玩家使用！");
                return true;
            } else if (!sender.hasPermission("playertracker.view")) {
                sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
                return true;
            }

            if (args.length == 0) {
                // 如果没有参数，显示正确用法
                sender.sendMessage(ChatColor.RED + "用法: /trackview <玩家名|stop>");
                return true;
            } else if (args[0].equalsIgnoreCase("stop")) {
                // 如果参数为 "stop" ，从 trackingMap 中删除追踪映射
                Player removedPlayer = viewer.trackingMap.remove(sender);
                if (removedPlayer != null) {
                    sender.sendMessage(ChatColor.YELLOW + "已停止追踪玩家 " + ChatColor.GREEN + removedPlayer.getName() + ChatColor.YELLOW + " 的坐标。");
                } else {
                    sender.sendMessage(ChatColor.RED + "你没有在追踪任何玩家。");
                }
                return true;
            } else {
                // 其他情况，认为参数为玩家名，向 trackingMap 中添加追踪映射
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    viewer.trackingMap.put((Player) sender, target);
                } else {
                    // 找不到玩家，报错
                    sender.sendMessage(ChatColor.RED + "玩家 " + args[0] + " 不在线或不存在！");
                }
                return true;
            }
        }

        return false; // 如果命令不匹配，返回 false
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

}