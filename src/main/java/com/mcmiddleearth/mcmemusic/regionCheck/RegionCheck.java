package com.mcmiddleearth.mcmemusic.regionCheck;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.data.Region;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.mcmiddleearth.mcmemusic.util.PlayMusic;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RegionCheck extends BukkitRunnable {

    private LoadRegion loadRegion;
    private PlayMusic playMusic;

    public RegionCheck(LoadRegion loadRegion, PlayMusic playMusic){
        this.loadRegion = loadRegion;
        this.playMusic = playMusic;
    }

    public void containCheck(Player p, Polygonal2DRegion region, Region musicRegion){
        int x, z;
        x = p.getLocation().getBlockX();
        z = p.getLocation().getBlockZ();
        BlockVector3 playerVector = BlockVector3.at(x, 0, z);

        if(region.contains(playerVector)) {
            if(!musicRegion.isListening(p)) {
                playMusic.playMusic(musicRegion, p);
            }
        } else {
            if(musicRegion.isListening(p)) {
                playMusic.stopMusic(musicRegion, p);
            }
        }
    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream()
              .filter(player -> !Main.getInstance().getPlayerManager().isDeafened(player))
              .forEach(player -> {
            loadRegion.getRegionsMap().forEach((k,v) -> containCheck(player,k,v));
        });
    }
}
