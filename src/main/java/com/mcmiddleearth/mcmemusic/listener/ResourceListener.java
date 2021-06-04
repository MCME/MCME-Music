package com.mcmiddleearth.mcmemusic.listener;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.util.ArrayList;
import java.util.List;

public class ResourceListener implements Listener {

    public static List<Player> resourceList = new ArrayList<>();

    @EventHandler
    public void onResourcepackStatusEvent(PlayerResourcePackStatusEvent e){
        if(e.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED){
            Player p = e.getPlayer(); p.sendMessage("player = " + p);
            resourceList.add(p); p.sendMessage("list = " + resourceList);
            p.sendMessage(ChatColor.GREEN + "Loaded Resource Pack");
        }
    }


}
