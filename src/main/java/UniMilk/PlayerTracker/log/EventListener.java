package unimilk.playertracker.log;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;

public class EventListener implements Listener {
    // 事件监听器类，用于处理玩家活动事件
    private final ActivityLogger logger;

    public EventListener(ActivityLogger logger) {
        // 构造函数，接收活动记录器实例
        this.logger = logger;
    }
    
    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event) {
        // 玩家加入游戏事件处理
        Player player = event.getPlayer();
        logger.log(player, "加入游戏");
    }

    @EventHandler
    public void onPlayerQuit(org.bukkit.event.player.PlayerQuitEvent event) {
        // 玩家离开游戏事件处理
        Player player = event.getPlayer();
        logger.log(player, "离开游戏");
    }

    @EventHandler
    public void onPlayerDie(org.bukkit.event.entity.PlayerDeathEvent event) {
        // 玩家死亡事件处理
        Player player = event.getEntity();
        logger.log(player, "死亡");
    }
    
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        // 玩家切换飞行状态事件处理
        Player player = event.getPlayer();
        logger.log(player, "切换飞行状态：" + (player.isFlying() ? "开启" : "关闭"));
    }

    @EventHandler
    public void onVehicleEnter(VehicleEnterEvent event) {
        // 玩家进入载具事件处理
        if (event.getEntered() instanceof Player) {
            Player player = (Player) event.getEntered();
            logger.log(player, "进入载具：" + event.getVehicle().getType()); // 记录进入的载具类型
        }
    }

    @EventHandler
    public void onVehicleExit(VehicleExitEvent event) {
        // 玩家退出载具事件处理
        if (event.getExited() instanceof Player) {
            Player player = (Player) event.getExited();
            logger.log(player, "退出载具：" + event.getVehicle().getType()); // 记录退出的载具类型
        }
    }

    @EventHandler
    public void onPlayerHurt(EntityDamageEvent event) {
        // 玩家受伤事件处理
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            logger.log(player, "受伤");
        }
    }

    @EventHandler
    public void onPlayerUseEnderPearl(PlayerInteractEvent event) {
        // 玩家使用末影珍珠事件处理
        if (event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL) {
            Player player = event.getPlayer();
            logger.log(player, "使用末影珍珠");
        }
    }

    @EventHandler
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        // 玩家攻击事件处理
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            logger.log(player, "攻击了：" + event.getEntity().getName()); // 记录攻击的目标
        }
    }
    
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        // 玩家打开箱子事件处理
        if (event.getInventory().getType() == InventoryType.CHEST) {
            Player player = (Player) event.getPlayer();
            logger.log(player, "打开了箱子"); // 记录打开箱子的事件
        }
    }
}
