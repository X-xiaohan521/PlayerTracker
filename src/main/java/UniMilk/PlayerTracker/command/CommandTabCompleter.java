package unimilk.playertracker.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // 补全 `/playertracker` 或 `/pt`
        if (args.length == 1) {
            return Arrays.asList("track", "viewer", "log", "reload", "help");
        }

        // 补全
        
        return new ArrayList<>();
    }
}
