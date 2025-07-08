package unimilk.playertracker.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

import unimilk.playertracker.PlayerTracker;
import unimilk.playertracker.util.PlayerStatusUtils;


public class CommandHandler implements CommandExecutor{
    private final PlayerTracker plugin; // 定义插件实例

    public CommandHandler(PlayerTracker plugin) {
        // 构造函数，接收插件实例
        this.plugin = plugin; // 初始化插件实例
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            // 如果无任何参数，输出用法
            sender.sendMessage(ChatColor.RED + "用法：/playertracker <track|viewer|log> ...");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "track":
                // 调用handleTrackCommand方法处理 `/playertracker track` 命令
                return handleTrackCommand(sender, args);
            case "viewer":
                // 调用handleViewerCommand方法处理 `/playertracker viewer` 命令
                return handleViewerCommand(sender, args);
            case "log":
                // 调用handleLogCommand方法处理 `/playertracker log` 命令
                return handleLogCommand(sender, args);
            case "reload":
                // 调用handleReloadCommand方法处理 `/playertracker reload` 命令
                return handleReloadCommand(sender);
            default:
                sender.sendMessage(ChatColor.RED + "未知命令，请使用 /playertracker help 查看帮助。");
        }

        return true;
    }

    private boolean handleTrackCommand(CommandSender sender, String[] args) {
        // 处理 `/playertracker track` 命令
        
        // 只有拥有 "playertracker.use" 权限的玩家和服务器控制台可以使用该命令
        if (!sender.hasPermission("playertracker.use") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
                return true;
            }

        if (args.length == 1) {
            // 没有参数时，查询所有在线玩家
            sender.sendMessage(ChatColor.GOLD + "===== 在线玩家信息 =====");
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerStatusUtils.sendPlayerInfo(sender, player);
            }
        } else {
            // 有参数时，查询特定玩家
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "玩家 " + args[1] + " 不在线或不存在！");
            } else {
                PlayerStatusUtils.sendPlayerInfo(sender, target);
            }
        }
        return true;
    }

    private boolean handleViewerCommand(CommandSender sender, String[] args) {
        // 处理 `/playertracker viewer` 命令

        // 只有拥有 playertracker.view 权限的玩家才能使用此命令
        if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
            sender.sendMessage(ChatColor.RED + "该命令只能由玩家使用！");
            return true;
        } else if (!sender.hasPermission("playertracker.view")) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
            return true;
        }

        if (args.length == 1) {
            // 如果没有参数，显示正确用法
            sender.sendMessage(ChatColor.RED + "用法: /playertracker viewer <add|remove> <player>");
            return true;
        } else if (args[1].equalsIgnoreCase("remove")) {
            // 如果参数为 "remove" ，从 trackingMap 中删除追踪映射
            Player removedPlayer = plugin.viewer.removeTracker((Player) sender);
            if (removedPlayer != null) {
                sender.sendMessage(ChatColor.YELLOW + "已停止追踪玩家 " + ChatColor.GREEN + removedPlayer.getName() + ChatColor.YELLOW + " 。");
            } else {
                sender.sendMessage(ChatColor.RED + "你没有在追踪任何玩家。");
            }
            return true;
        } else if (args[1].equalsIgnoreCase("add")) {
            // 如果参数为 "add" ，向 trackingMap 中添加追踪映射
            Player target = Bukkit.getPlayer(args[2]);
            if (target != null) {
                plugin.viewer.addTracker((Player) sender, target);
                sender.sendMessage(ChatColor.YELLOW + "开始追踪玩家 " + ChatColor.GREEN + target.getName() + ChatColor.YELLOW + " 。");
            } else {
                // 找不到玩家，报错
                sender.sendMessage(ChatColor.RED + "玩家 " + args[2] + " 不在线或不存在！");
            }
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "用法: /playertracker viewer <add|remove> <player>");
            return true;
        }
    }

    private boolean handleLogCommand(CommandSender sender, String[] args) {
        // 处理 `/playertracker log` 命令
        
        // 检查是否有权限使用该命令
        if (!sender.hasPermission("playertracker.admin") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
            return true;
        }

        // 如果没有参数，显示当前日志记录状态
        if (args.length == 1) {
            sender.sendMessage(ChatColor.YELLOW + "日志记录状态： " + (plugin.getConfigBoolean("log.enabled") ? ChatColor.GREEN + "已启用" : ChatColor.RED + "已禁用"));
            return true;
        }
        
        // 如果有参数，设置日志记录状态，并保存到配置文件
        if (args[1].equalsIgnoreCase("on")) {
            plugin.setConfig("log.enabled", true);
            sender.sendMessage(ChatColor.YELLOW + "日志记录状态： " + (plugin.getConfigBoolean("log.enabled") ? ChatColor.GREEN + "已启用" : ChatColor.RED + "已禁用"));
        } else if (args[1].equalsIgnoreCase("off")) {
            plugin.setConfig("log.enabled", false);
            sender.sendMessage(ChatColor.YELLOW + "日志记录状态： " + (plugin.getConfigBoolean("log.enabled") ? ChatColor.GREEN + "已启用" : ChatColor.RED + "已禁用"));
        } else {
            sender.sendMessage(ChatColor.RED + "用法: /playertracker log <on|off>");
        }
        return true;
    }

    private boolean handleReloadCommand(CommandSender sender) {
        // 处理 `/playertracker reload` 命令

        // 检查是否有权限使用该命令
        if (!sender.hasPermission("playertracker.admin") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
            return true;
        }

        plugin.reloadConfig();
        plugin.onConfigReload();
        sender.sendMessage(ChatColor.YELLOW + "配置文件已重新加载！");
        return true;

    }
}
