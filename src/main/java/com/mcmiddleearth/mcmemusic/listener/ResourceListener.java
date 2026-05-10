package com.mcmiddleearth.mcmemusic.listener;

import com.mcmiddleearth.mcmemusic.data.Region;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

public class ResourceListener implements Listener {

    public static List<Player> resourceList = new ArrayList<>();
    public static HashMap<Player, Region> playerRegions = new HashMap<>();

    @EventHandler
    public void onResourcePackStatusEvent(PlayerResourcePackStatusEvent e) {
        if (e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            Player p = e.getPlayer();
            resourceList.add(p);

            Region region = playerRegions.get(p);

            try {
                region.removeListeningPlayer(p);
                p.sendMessage(Component.text("Refreshing Music...", NamedTextColor.GREEN));
            } catch (NullPointerException error) {
                // Player not in Region
            }
        }
    }
}
