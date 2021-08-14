package com.mcmiddleearth.mcmemusic.listener;

import com.mcmiddleearth.mcmemusic.data.Region;
import com.mcmiddleearth.mcmemusic.regionCheck.RegionCheck;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ResourceListener implements Listener {

    private final RegionCheck regionCheck;
    public static List<Player> resourceList = new ArrayList<>();
    public static HashMap<Player, Region> playerRegions = new HashMap<>();

    public ResourceListener(RegionCheck regionCheck){
        this.regionCheck = regionCheck;
    }

    @EventHandler
    public void onResourcepackStatusEvent(PlayerResourcePackStatusEvent e){
        if(e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            Player p = e.getPlayer();
            resourceList.add(p);

            Region region = playerRegions.get(p);

            try{
                region.removeListeningPlayer(p);
                p.sendMessage(ChatColor.GREEN + "Refreshing Music...");
            }catch(NullPointerException error){
                //Player not in Region
            }
        }
    }
}
