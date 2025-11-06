package unimilk.playertracker.core;

import unimilk.playertracker.api.util.IPlayerStatusUtils;

import org.bukkit.Bukkit;

public class VersionManager {
    public static void init() {
        String version = Bukkit.getServer().getBukkitVersion();
        if (version.startsWith("1.13") || version.startsWith("1.14") ||
            version.startsWith("1.15")) {
            loadImpl("unimilk.playertracker.impl.v1_13_15");
        } else {
            loadImpl("unimilk.playertracker.impl.v1_16_21");
        }
    }

    private static void loadImpl(String basePackage) {
        // 使用反射或 ServiceLoader 动态加载实现类
        String className = basePackage + ".util.PlayerStatusUtilsImpl";
        try {
            IPlayerStatusUtils utils = (IPlayerStatusUtils)
            Class.forName(className).getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot load version class: " + className, e);
        }

    }

    public static IPlayerStatusUtils getPlayerStatusUtils() {
        return playerStatusUtils;
    }
}
