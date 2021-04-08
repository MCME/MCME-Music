package com.mcmiddleearth.mcmemusic.regionCheck;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.data.Region;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.mcmiddleearth.mcmemusic.util.PlayMusic;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class RegionCheck extends BukkitRunnable {

    private LoadRegion loadRegion;
    private PlayMusic playMusic;
    private Main main;

    int serverTime = 0;
    HashMap<Player, Integer> playerLoopTime = new HashMap<Player, Integer>();

    public RegionCheck(LoadRegion loadRegion, PlayMusic playMusic, Main main){
        this.loadRegion = loadRegion;
        this.playMusic = playMusic;
        this.main = main;
    }

    public void containCheck(Player p, Polygonal2DRegion region, Region musicRegion){
        int x, z;
        int musicID = musicRegion.getMusicID();
        x = p.getLocation().getBlockX();
        z = p.getLocation().getBlockZ();
        BlockVector3 playerVector = BlockVector3.at(x, 0, z);

        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(musicID));
        int loop = path.getInt("loop");

        if(region.contains(playerVector)) {
            if(!musicRegion.isListening(p)) {
                playMusic.playMusic(musicRegion, p);
                playerLoopTime.put(p, serverTime + (loop/2));
            }
            else{
                try{
                    if(playerLoopTime.get(p).equals(serverTime)){
                        playerLoopTime.remove(p);
                        playMusic.stopMusic(musicRegion, p);
                        playMusic.playMusic(musicRegion, p);
                        playerLoopTime.put(p, serverTime + (loop/2));
                    }
                } catch(NullPointerException e){
                    //Player doesn't need to loop
                }
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

        serverTime++;
    }
}
