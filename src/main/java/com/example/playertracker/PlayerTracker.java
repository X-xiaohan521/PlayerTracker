package me.yourname.playertracker;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerTracker extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        this.getCommand("track").setExecutor(this);
        getLogger().info("PlayerTracker 插件已启用！");
    }

    @Override
    public void onDisable() {
        getLogger().info("PlayerTracker 插件已禁用！");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("playertracker.use") && !(sender instanceof org.bukkit.command.ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用此命令！");
            return true;
        }

        if (args.length == 0) {
            // 查询所有在线玩家
            sender.sendMessage(ChatColor.GOLD + "===== 在线玩家信息 =====");
            for (Player player : Bukkit.getOnlinePlayers()) {
                sendPlayerInfo(sender, player);
            }
        } else {
            // 查询特定玩家
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "玩家 " + args[0] + " 不在线或不存在！");
            } else {
                sendPlayerInfo(sender, target);
            }
        }
        return true;
    }

    private void sendPlayerInfo(CommandSender sender, Player player) {
        Location loc = player.getLocation();
        String activity = getActivity(player);

        sender.sendMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW +
                " - 坐标: [" + ChatColor.AQUA + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ChatColor.YELLOW + "]" +
                " - 当前状态: " + ChatColor.LIGHT_PURPLE + activity);
    }

    private String getActivity(Player player) {
        if (player.isSleeping()) return "睡觉中";
        if (player.isInsideVehicle()) return "乘坐载具";
        if (player.isSwimming()) return "游泳";
        if (player.isGliding()) return "滑翔中";
        if (player.isFlying()) return "飞行中";
        if (player.isSprinting()) return "疾跑";
        if (player.isSneaking()) return "潜行";
        return "闲逛";
    }
}
