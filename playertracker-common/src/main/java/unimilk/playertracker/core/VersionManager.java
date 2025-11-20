package unimilk.playertracker.core;

import unimilk.playertracker.api.util.IMessageSender;
import unimilk.playertracker.api.util.IPlayerStatusManager;
import unimilk.playertracker.util.PlayerStatusManager;

import org.bukkit.Bukkit;

public class VersionManager {
    private IMessageSender messageSender;
    private PlayerStatusManager playerStatusManager;
    
    public VersionManager(PlayerStatusManager playerStatusManager) {
        this.playerStatusManager = playerStatusManager;
        String version = Bukkit.getServer().getBukkitVersion();
        if (version.startsWith("1.13") || version.startsWith("1.14") ||
            version.startsWith("1.15")) {
            loadImpl("unimilk.playertracker.impl.v1_13_15");
        } else {
            loadImpl("unimilk.playertracker.impl.v1_16_21");
        }
    }

    private void loadImpl(String basePackage) {
        // 使用反射动态加载实现类
        String className = basePackage + ".util.MessageSenderImpl";
        try {
            this.messageSender = (IMessageSender) Class.forName(className).getMethod("newMessageSender", IPlayerStatusManager.class).invoke(null, playerStatusManager);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load version class: " + className, e);
        }
    }

    public IMessageSender getMessageSender() {
        return this.messageSender;
    }
}