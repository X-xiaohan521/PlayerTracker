package unimilk.playertracker.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;
import unimilk.playertracker.util.PlayerStatusUtils;


public class CommandHandler implements CommandExecutor{
    private final JavaPlugin plugin; // 定义插件实例

    public CommandHandler(JavaPlugin plugin) {
        // 构造函数，接收插件实例
        this.plugin = plugin; // 初始化插件实例
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            // 如果无任何参数，输出用法
            sender.sendMessage("/playertracker <track|viewer|log> ...");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "track":
                // 处理 `/playertracker track` 命令
                
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

            case "viewer":
                if (args.length < 3) {
                    player.sendMessage("/playertracker viewer <add|remove> <player>");
                    return true;
                }
                if (args[1].equalsIgnoreCase("add")) {
                    addTracking(player, args[2]);
                } else if (args[1].equalsIgnoreCase("remove")) {
                    removeTracking(player, args[2]);
                }
                return true;
            
            case "log":
                return true;
            
            default:
                player.sendMessage(ChatColor.RED + "未知命令，请使用 /playertracker help 查看帮助");
                return true;
        }
    }
}
