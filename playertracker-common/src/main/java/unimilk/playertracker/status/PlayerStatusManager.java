package unimilk.playertracker.status;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import unimilk.playertracker.PlayerTracker;
import unimilk.playertracker.api.util.IPlayerStatusManager;

public class PlayerStatusManager implements IPlayerStatusManager {
    private final PlayerTracker plugin;
    private Map<Player, Boolean> eatingMap = new HashMap<>();
    private Map<Player, PlayingWith> playingMap = new HashMap<>();
    
    public PlayerStatusManager(PlayerTracker plugin) {
        this.plugin = plugin;
    }

    public String getStatus(Player player) {
        // 获取玩家当前活动状态方法
        if (this.isEating(player)) return "进食中";
        if (this.isPlayingWith(player).equals(PlayingWith.CAT)) return "在和小猫玩";
        if (this.isPlayingWith(player).equals(PlayingWith.DOG)) return "在和小狗玩";
        if (this.isPlayingWith(player).equals(PlayingWith.PARROT)) return "在和小鹦鹉玩";
        if (player.isSleeping()) return "睡觉中";
        if (player.isInsideVehicle()) return "乘坐载具";
        if (player.isSwimming()) return "游泳";
        if (player.isGliding()) return "滑翔中";
        if (player.isFlying()) return "飞行中";
        if (player.isSprinting()) return "疾跑";
        if (player.isSneaking()) return "潜行";
        return "闲逛";
    }

    public void setEating(Player player, boolean isEating) {
        eatingMap.put(player, isEating);
    }

    public void clearEating(Player player) {
        eatingMap.remove(player);
    }

    public boolean isEating(Player player) {
        return eatingMap.getOrDefault(player, false);
    }

    public void setPlaying(Player player, PlayingWith playingWith, Tameable entity) {
        playingMap.put(player, playingWith);
        startCheckingIfStillPlaying(entity, player);
    }

    public void clearPlaying(Player player) {
        playingMap.remove(player);
    }

    public PlayingWith isPlayingWith(Player player) {
        return playingMap.getOrDefault(player, null);
    }

    private BukkitTask startCheckingIfStillPlaying(Entity entity, Player player) {
        BukkitTask checkIfStillPlaying = new BukkitRunnable() {
            @Override
            public void run() {
                if (!entity.isValid() || !player.isOnline()) {
                    clearPlaying(player);
                    cancel();
                }

                if (entity.getLocation().distance(player.getLocation()) > 10) {
                    clearPlaying(player);
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 20L);
        return checkIfStillPlaying;
    }

    public String getCoords(Player player) {
        // 获取玩家坐标方法
        Location loc = player.getLocation();
        int x = loc.getBlockX(); // 获取玩家所在世界的X坐标
        int y = loc.getBlockY(); // 获取玩家所在世界的Y坐标
        int z = loc.getBlockZ(); // 获取玩家所在世界的Z坐标
        return String.format("%s %s %s", x, y, z);
    }
}
