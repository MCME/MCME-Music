package com.mcmiddleearth.mcmemusic.listeners;

import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.mcmiddleearth.mcmemusic.util.PlayMusic;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.logging.Logger;

public class RegionCheck implements Listener {

    private LoadRegion loadRegion;
    private PlayMusic playMusic;
    private Logger log;

    public RegionCheck(LoadRegion loadRegion, PlayMusic playMusic){
        this.loadRegion = loadRegion;
        this.playMusic = playMusic;
    }

    public void containCheck(Player p, Polygonal2DRegion region, Integer musicID){
        int x, z;
        x = p.getLocation().getBlockX();
        z = p.getLocation().getBlockZ();
        BlockVector3 playerVector = BlockVector3.at(x, 0, z);

        if(region.contains(playerVector)){
            p.sendMessage("You are in a region with music ID of " + musicID);
            playMusic.playMusic(musicID, p);
            //Run method that plays music

        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        loadRegion.allRegions.forEach((k, v) -> containCheck(p, k, v));
    }

}
