package unimilk.playertracker.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import unimilk.playertracker.PlayerTracker;
import unimilk.playertracker.util.PlayerStatusUtils;
import unimilk.playertracker.viewer.TrackViewer;


public class CommandHandler implements CommandExecutor{
    private final PlayerTracker plugin; // 定义插件实例
    private final TrackViewer viewer;

    public CommandHandler(PlayerTracker plugin, TrackViewer viewer) {
        // 构造函数，接收插件实例
        this.plugin = plugin; // 初始化插件实例
        this.viewer = viewer; // 初始化追踪器实例
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            // 如果无任何参数，输出用法
            plugin.adventure().sender(sender).sendMessage(Component.text("用法：/playertracker <track|viewer|log> ...", NamedTextColor.RED));
            return true;
        } else if (args[0].equalsIgnoreCase("reload")) {
            // 调用handleReloadCommand方法处理 `/playertracker reload` 命令
            return handleReloadCommand(sender);
        } else if (!plugin.isPluginEnabled()) {
            plugin.adventure().sender(sender).sendMessage(Component.text("PlayerTracker 插件已禁用！", NamedTextColor.RED));
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "track":
                    // 调用handleTrackCommand方法处理 `/playertracker track` 命令
                    if (plugin.isPluginEnabled()) return handleTrackCommand(sender, args);
                case "viewer":
                    // 调用handleViewerCommand方法处理 `/playertracker viewer` 命令
                    return handleViewerCommand(sender, args);
                case "log":
                    // 调用handleLogCommand方法处理 `/playertracker log` 命令
                    return handleLogCommand(sender, args);
                default:
                    plugin.adventure().sender(sender).sendMessage(Component.text("未知命令，请使用 /playertracker help 查看帮助。", NamedTextColor.RED));
            }
        }
        return true;
    }

    private boolean handleTrackCommand(CommandSender sender, String[] args) {
        // 处理 `/playertracker track` 命令
        
        // 只有拥有 "playertracker.use" 权限的玩家和服务器控制台可以使用该命令
        if (!sender.hasPermission("playertracker.use") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
                plugin.adventure().sender(sender).sendMessage(Component.text("你没有权限使用此命令！", NamedTextColor.RED));
                return true;
            }

        if (args.length == 1) {
            // 没有参数时，查询所有在线玩家
            plugin.adventure().sender(sender).sendMessage(Component.text("===== 在线玩家信息 =====", NamedTextColor.GOLD));
            for (Player player : Bukkit.getOnlinePlayers()) {
                PlayerStatusUtils.sendPlayerInfo(sender, player, plugin);
            }
        } else {
            // 有参数时，查询特定玩家
            Player target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                plugin.adventure().sender(sender).sendMessage(
                    Component.text("玩家 " + args[1] + " 不在线或不存在！", NamedTextColor.RED)
                );
            } else {
                PlayerStatusUtils.sendPlayerInfo(sender, target, plugin);
            }
        }
        return true;
    }

    private boolean handleViewerCommand(CommandSender sender, String[] args) {
        // 处理 `/playertracker viewer` 命令

        // 只有拥有 playertracker.view 权限的玩家才能使用此命令
        if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
            plugin.adventure().sender(sender).sendMessage(Component.text("该命令只能由玩家使用！", NamedTextColor.RED));
            return true;
        } else if (!sender.hasPermission("playertracker.view")) {
            plugin.adventure().sender(sender).sendMessage(Component.text("你没有权限使用此命令！", NamedTextColor.RED));
            return true;
        }

        if (args.length == 1) {
            // 如果没有参数，显示正确用法
            plugin.adventure().sender(sender).sendMessage(Component.text("用法: /playertracker viewer <add|remove> <player>", NamedTextColor.RED));
            return true;
        } else if (args[1].equalsIgnoreCase("remove")) {
            // 如果参数为 "remove" ，从 trackingMap 中删除追踪映射
            Player removedPlayer = viewer.removeTracker((Player) sender);
            if (removedPlayer != null) {
                plugin.adventure().sender(sender).sendMessage(
                    Component.text("已停止追踪玩家 ", NamedTextColor.YELLOW)
                    .append(Component.text(removedPlayer.getName(), NamedTextColor.GREEN))
                    .append(Component.text(" 。", NamedTextColor.YELLOW))
                );
            } else {
                plugin.adventure().sender(sender).sendMessage(Component.text("你没有在追踪任何玩家。", NamedTextColor.RED));
            }
            return true;
        } else if (args[1].equalsIgnoreCase("add")) {
            // 如果参数为 "add" ，向 trackingMap 中添加追踪映射
            Player target = Bukkit.getPlayer(args[2]);
            if (target != null) {
                viewer.addTracker((Player) sender, target);
                plugin.adventure().sender(sender).sendMessage(
                    Component.text("开始追踪玩家 ", NamedTextColor.YELLOW)
                    .append(Component.text(target.getName(), NamedTextColor.GREEN))
                    .append(Component.text(" 。", NamedTextColor.YELLOW))
                );
            } else {
                // 找不到玩家，报错
                plugin.adventure().sender(sender).sendMessage(
                    Component.text("玩家 " + args[2] + " 不在线或不存在！", NamedTextColor.RED)
                );
            }
            return true;
        } else {
            plugin.adventure().sender(sender).sendMessage(
                Component.text("用法: /playertracker viewer <add|remove> <player>", NamedTextColor.RED)
            );
            return true;
        }
    }

    private boolean handleLogCommand(CommandSender sender, String[] args) {
        // 处理 `/playertracker log` 命令
        
        // 检查是否有权限使用该命令
        if (!sender.hasPermission("playertracker.admin") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            plugin.adventure().sender(sender).sendMessage(Component.text("你没有权限使用此命令！", NamedTextColor.RED));
            return true;
        }

        // 如果没有参数，显示当前日志记录状态
        if (args.length == 1) {
            plugin.adventure().sender(sender).sendMessage(
                Component.text("日志记录状态： ", NamedTextColor.YELLOW)
                .append(plugin.getConfigBoolean("log.enabled") ? Component.text("已启用", NamedTextColor.GREEN) : Component.text("已禁用", NamedTextColor.RED))
            );
            return true;
        }
        
        // 如果有参数，设置日志记录状态，并保存到配置文件
        if (args[1].equalsIgnoreCase("on")) {
            plugin.setConfig("log.enabled", true);
            plugin.adventure().sender(sender).sendMessage(
                Component.text("日志记录状态： ", NamedTextColor.YELLOW)
                .append(plugin.getConfigBoolean("log.enabled") ? Component.text("已启用", NamedTextColor.GREEN) : Component.text("已禁用", NamedTextColor.RED))
            );
        } else if (args[1].equalsIgnoreCase("off")) {
            plugin.setConfig("log.enabled", false);
            plugin.adventure().sender(sender).sendMessage(
                Component.text("日志记录状态： ", NamedTextColor.YELLOW)
                .append(plugin.getConfigBoolean("log.enabled") ? Component.text("已启用", NamedTextColor.GREEN) : Component.text("已禁用", NamedTextColor.RED))
            );
        } else if (args[1].equalsIgnoreCase("schedule")) {
            if (args.length == 2) {
                plugin.adventure().sender(sender).sendMessage(
                    Component.text("日志记录间隔： ", NamedTextColor.YELLOW)
                    .append(Component.text(plugin.getConfigInt("log.schedule"), NamedTextColor.GREEN))
                );
                return true;
            }
            else {
                try {
                    int schedule = Integer.parseInt(args[2]);
                    plugin.setConfig("log.schedule", schedule);
                    plugin.adventure().sender(sender).sendMessage(
                        Component.text("日志记录间隔： ", NamedTextColor.YELLOW)
                        .append(Component.text(plugin.getConfigInt("log.schedule"), NamedTextColor.GREEN))
                    );
                } catch (NumberFormatException e) {
                    plugin.adventure().sender(sender).sendMessage(
                        Component.text("用法: /playertracker log schedule <int>(s)", NamedTextColor.RED)
                    );
                }
            }
        } else {
            plugin.adventure().sender(sender).sendMessage(
                Component.text("用法: /playertracker log <on|off|schedule>", NamedTextColor.RED)
            );
        }
        return true;
    }

    private boolean handleReloadCommand(CommandSender sender) {
        // 处理 `/playertracker reload` 命令

        // 检查是否有权限使用该命令
        if (!sender.hasPermission("playertracker.admin") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            plugin.adventure().sender(sender).sendMessage(Component.text("你没有权限使用此命令！", NamedTextColor.RED));
            return true;
        }

        // 调用插件重载函数
        plugin.onConfigReload();
        plugin.adventure().sender(sender).sendMessage(
            Component.text("配置文件已重新加载！状态： ", NamedTextColor.YELLOW)
            .append(plugin.isPluginEnabled() ? Component.text("已启用", NamedTextColor.GREEN) : Component.text("已禁用", NamedTextColor.RED))
        );
        return true;

    }
}
