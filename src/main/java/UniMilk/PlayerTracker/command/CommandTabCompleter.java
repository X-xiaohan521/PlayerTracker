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

public class CommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // 补全 `/playertracker ?` 或 `/pt ?`
        if (args.length == 1) {
            return Arrays.asList("track", "viewer", "log", "reload", "help");
        }

        // 补全 `/playertracker track ?` 或 `/pt track ?`
        if (args.length == 2 && args[0].equalsIgnoreCase("track")) {
            return getPlayerNameList(args[1]);
        }

        // 补全 `/playertracker viewer ?` 或 `/pt viewer ?`
        if (args.length == 2 && args[0].equalsIgnoreCase("viewer")) {
            return Arrays.asList("add", "remove");
        }
        // 补全 `/pt viewer add/remove ?`
        if (args.length == 3 && args[0].equalsIgnoreCase("viewer")) {
            return getPlayerNameList(args[2]);
        }

        // 补全 `/playertracker log ?` 或 `/pt log ?`
        if (args.length == 2 && args[0].equalsIgnoreCase("log")) {
            return Arrays.asList("on", "off");
        }

        return new ArrayList<>();
    }

    public List<String> getPlayerNameList(String initial) {
        return Bukkit.getOnlinePlayers().stream()
            .sorted(Comparator.comparing(Player::getName))
            .map(Player::getName)
            .filter(name -> name.toLowerCase().startsWith(initial))
            .collect(Collectors.toList());
    }
}
