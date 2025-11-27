package unimilk.playertracker.core;

import unimilk.playertracker.api.util.ICatListener;
import unimilk.playertracker.api.util.IMessageSender;
import unimilk.playertracker.api.util.IPlayerStatusManager;
import unimilk.playertracker.log.ActivityLogger;
import unimilk.playertracker.status.PlayerStatusManager;

import org.bukkit.Bukkit;

public class VersionManager {
    private ActivityLogger logger;
    private IMessageSender messageSender;
    private PlayerStatusManager playerStatusManager;
    private ICatListener catListener;
    
    public VersionManager(PlayerStatusManager playerStatusManager) {
        this.playerStatusManager = playerStatusManager;
        String version = Bukkit.getServer().getBukkitVersion();
        if (version.startsWith("1.13")) {
            loadImpl("unimilk.playertracker.impl.v1_13");
        } else if (version.startsWith("1.14") || version.startsWith("1.15")) {
            loadImpl("unimilk.playertracker.impl.v1_14_15");
        } else {
            loadImpl("unimilk.playertracker.impl.v1_16_21");
        }
    }

    public void setActivityLogger(ActivityLogger logger) {
        this.logger = logger;
    }

    private void loadImpl(String basePackage) {
        // 使用反射动态加载 MessageSender
        String classNameOfMessageSender = basePackage + ".util.MessageSenderImpl";
        try {
            this.messageSender = (IMessageSender) Class.forName(classNameOfMessageSender).getMethod("newMessageSender", IPlayerStatusManager.class).invoke(null, playerStatusManager);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load version class: " + classNameOfMessageSender, e);
        }

        // 使用反射动态加载 CatListener
        String classNameOfCatListener = basePackage + ".util.CatListenerImpl";
        try {
            this.catListener = (ICatListener) Class.forName(classNameOfCatListener).getConstructor(PlayerStatusManager.class, ActivityLogger.class).newInstance(playerStatusManager, logger);
        } catch (Exception e) {
            throw new RuntimeException("Cannot load version class: " + classNameOfCatListener, e);
        }
    }

    public IMessageSender getMessageSender() {
        return this.messageSender;
    }

    public ICatListener getCatListener() {
        return this.catListener;
    }
}