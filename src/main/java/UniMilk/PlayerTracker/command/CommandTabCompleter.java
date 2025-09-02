package unimilk.playertracker.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import unimilk.playertracker.PlayerTracker;

public class CommandTabCompleter implements TabCompleter {
    private PlayerTracker plugin;
    
    public CommandTabCompleter(PlayerTracker plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!plugin.isPluginEnabled()) {
            // 如果插件未启用，则提醒管理员可以 reload 插件
            if (sender.hasPermission("playertracker.admin")) {
                return getListStartsWith(Arrays.asList("reload"), args[0]);
            } else {
                return Arrays.asList("");
            }
        }
        
        // 补全 `/playertracker ?` 或 `/pt ?`
        if (args.length == 1) {
            List<String> list = new ArrayList<>();
            if (sender.hasPermission("playertracker.use")) {
                // 如果玩家有 "playertracker.use" 权限，添加 track 和 help 命令
                list.add("track");
                list.add("help");
            }
            if (sender.hasPermission("playertracker.admin")) {
                // 如果玩家有 "playertracker.admin" 权限，添加 log 和 reload 命令
                list.add("log");
                list.add("reload");
            }
            if (sender.hasPermission("playertracker.view")) {
                // 如果玩家有 "playertracker.view" 权限，添加 viewer 命令
                list.add("viewer");
            }
            return getListStartsWith(list, args[0]);
        }

        // 补全 `/playertracker track ?` 或 `/pt track ?`
        if (args.length == 2 && args[0].equalsIgnoreCase("track") && sender.hasPermission("playertracker.use")) {
            return getPlayerNameList(args[1]);
        }

        // 补全 `/playertracker viewer ?` 或 `/pt viewer ?`
        if (args.length == 2 && args[0].equalsIgnoreCase("viewer") && sender.hasPermission("playertracker.view")) {
            return getListStartsWith(Arrays.asList("add", "remove"), args[1]);
        }
        // 补全 `/pt viewer add/remove ?`
        if (args.length == 3 && args[0].equalsIgnoreCase("viewer") && sender.hasPermission("playertracker.view")) {
            return getPlayerNameList(args[2]);
        }

        // 补全 `/playertracker log ?` 或 `/pt log ?`
        if (args.length == 2 && args[0].equalsIgnoreCase("log") && sender.hasPermission("playertracker.admin")) {
            return getListStartsWith(Arrays.asList("on", "off", "schedule"), args[1]);
        }
        // 补全 `/pt log schedule`
        if (args.length == 3 && args[0].equalsIgnoreCase("log") && sender.hasPermission("playertracker.admin")) {
            return Arrays.asList("<int>");
        }

        return new ArrayList<>();
    }

    private List<String> getPlayerNameList(String initial) {
        return Bukkit.getOnlinePlayers().stream()
            .sorted(Comparator.comparing(Player::getName))
            .map(Player::getName)
            .filter(name -> name.toLowerCase().startsWith(initial))
            .collect(Collectors.toList());
    }

    private List<String> getListStartsWith(List<String> list,String initial) {
        return list.stream()
            .filter(name -> name.toLowerCase().startsWith(initial))
            .collect(Collectors.toList());
    }
}
