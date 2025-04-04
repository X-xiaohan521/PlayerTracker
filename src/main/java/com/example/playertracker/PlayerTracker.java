package UniMilk.playertracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class PlayerTracker extends JavaPlugin implements CommandExecutor, Listener {

    private FileConfiguration config; // 初始化配置文件对象

    @Override
    public void onEnable() {
        // 插件加载时的初始化逻辑
        getLogger().info("PlayerTracker 插件正在加载...");
        loadConfig(); // 加载配置文件
        getServer().getPluginManager().registerEvents(new PlayerListener(), this); // 注册事件监听器
        scheduleLogging(); // 启动定时记录任务
        getLogger().info("PlayerTracker 插件加载完毕！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // 处理 /track 命令
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
                    sendPlayerInfo(sender, player);
                }
            } else {
                // 有参数时，查询特定玩家
                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    sender.sendMessage(ChatColor.RED + "玩家 " + args[0] + " 不在线或不存在！");
                } else {
                    sendPlayerInfo(sender, target);
                }
            }
            return true;
        }
        
        // 处理 /tracklog 命令
        if (command.getName().equalsIgnoreCase("tracklog")) {
            // 检查是否有权限使用 /tracklog 命令
            if (!sender.hasPermission("playertracker.admin") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
                return true;
            }

            // 如果没有参数，显示当前日志记录状态
            if (args.length == 0) {
                sender.sendMessage(ChatColor.GREEN + "日志记录状态： " + (config.getBoolean("log.enabled") ? "已启用" : "已禁用"));
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

    private void sendPlayerInfo(CommandSender sender, Player player) {
        // 发送玩家信息函数
        Location loc = player.getLocation();
        String activity = getActivity(player);

        sender.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW +
                " - 坐标: [" + ChatColor.AQUA + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ChatColor.YELLOW + "]" +
                " - 当前状态: " + ChatColor.LIGHT_PURPLE + activity);
    }

    private String getActivity(Player player) {
        // 获取玩家当前活动状态函数
        if (player.isSleeping()) return "睡觉中";
        if (player.isInsideVehicle()) return "乘坐载具";
        if (player.isSwimming()) return "游泳";
        if (player.isGliding()) return "滑翔中";
        if (player.isFlying()) return "飞行中";
        if (player.isSprinting()) return "疾跑";
        if (player.isSneaking()) return "潜行";
        return "闲逛";
    }

    private void logPlayerActivity(Player player, String activity) {
        // 日志记录函数
        if (!config.getBoolean("log.enabled")) return; // 如果日志记录未启用，则返回
        
        // 获取当前时间并格式化
        String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());

        // 构建日志
        String logMessage = String.format("[%s] [%s] %s: %s", timeStamp, player.getName(), player.getLocation().toString(), activity); // 日志格式
        File logFile = new File(getDataFolder(), "player_activities.log"); // 日志文件路径

        // 使用 try-catch 块处理 IOException 异常
        try (FileWriter writer = new FileWriter(logFile, true)) { // 使用 try-with-resources 自动关闭资源
            writer.write(logMessage + "\n"); // 写入日志
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "无法写入日志文件！", e); // 捕获并记录错误
        }
    }

    private void scheduleLogging() {
        // 定时记录玩家活动函数
        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                String activity = getActivity(player); // 获取玩家活动状态
                logPlayerActivity(player, activity); // 记录日志
            }
        }, 0L, config.getInt("log.schedule")*20L); // 每隔指定时间记录一次活动
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

    private class PlayerListener implements Listener {
        // 事件监听器类，用于处理玩家活动事件
        @EventHandler
        public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
            // 玩家切换飞行状态事件处理
            Player player = event.getPlayer();
            logPlayerActivity(player, getActivity(player));
        }

        @EventHandler
        public void onVehicleEnter(VehicleEnterEvent event) {
            // 玩家进入载具事件处理
            if (event.getEntered() instanceof Player) {
                Player player = (Player) event.getEntered();
                logPlayerActivity(player, getActivity(player));
            }
        }

        @EventHandler
        public void onVehicleExit(VehicleExitEvent event) {
            // 玩家退出载具事件处理
            if (event.getExited() instanceof Player) {
                Player player = (Player) event.getExited();
                logPlayerActivity(player, getActivity(player));
            }
        }

        @EventHandler
        public void onPlayerHurt(EntityDamageEvent event) {
            // 玩家受伤事件处理
            if (event.getEntity() instanceof Player) {
                Player player = (Player) event.getEntity();
                logPlayerActivity(player, "受伤");
            }
        }

        @EventHandler
        public void onPlayerUseEnderPearl(PlayerInteractEvent event) {
            // 玩家使用末影珍珠事件处理
            if (event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL) {
                Player player = event.getPlayer();
                logPlayerActivity(player, "使用末影珍珠");
            }
        }

        @EventHandler
        public void onPlayerAttack(EntityDamageByEntityEvent event) {
            // 玩家攻击事件处理
            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();
                logPlayerActivity(player, "攻击了：" + event.getEntity().getName()); // 记录攻击的目标
            }
        }
        
        @EventHandler
        public void onInventoryOpen(InventoryOpenEvent event) {
            // 玩家打开箱子事件处理
            if (event.getInventory().getType() == InventoryType.CHEST) {
                Player player = (Player) event.getPlayer();
                logPlayerActivity(player, "打开了箱子"); // 记录打开箱子的事件
            }
        }
    }
}