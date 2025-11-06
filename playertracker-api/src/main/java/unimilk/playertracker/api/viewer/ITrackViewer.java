package unimilk.playertracker.api.viewer;

import org.bukkit.entity.Player;

public interface ITrackViewer {
    public void viewPlayerInfo(Player tracker, Player target);
    public void refreshTracker(Player player);
}