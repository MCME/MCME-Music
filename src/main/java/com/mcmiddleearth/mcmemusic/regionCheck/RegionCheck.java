package com.mcmiddleearth.mcmemusic.regionCheck;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.data.Region;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.mcmiddleearth.mcmemusic.util.PlayMusic;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class RegionCheck extends BukkitRunnable {

    private final LoadRegion loadRegion;
    private final PlayMusic playMusic;
    private final Main main;

    int serverTime = 0;
    HashMap<Player, Integer> playerLoopTime = new HashMap<Player, Integer>();

    public RegionCheck(LoadRegion loadRegion, PlayMusic playMusic, Main main){
        this.loadRegion = loadRegion;
        this.playMusic = playMusic;
        this.main = main;
    }

    public Region polyContainCheck(Player p, Polygonal2DRegion region, Region musicRegion){
        int x, z;
        int musicID = musicRegion.getMusicID();
        x = p.getLocation().getBlockX();
        z = p.getLocation().getBlockZ();
        BlockVector3 playerVector = BlockVector3.at(x, 0, z);

        if(region.contains(playerVector)) {
            return musicRegion;
        } else {
            if(musicRegion.isListening(p)) {
                playMusic.stopMusic(musicRegion, p);
            }
            return null;
        }
    }

    public Region cubeContainCheck(Player p, CuboidRegion region, Region musicRegion){
        int x, z, y;
        int musicID = musicRegion.getMusicID();
        x = p.getLocation().getBlockX();
        y = p.getLocation().getBlockY();
        z = p.getLocation().getBlockZ();
        BlockVector3 playerVector = BlockVector3.at(x, y, z);

        if(region.contains(playerVector)) {
            return musicRegion;
        } else {
            if(musicRegion.isListening(p)) {
                playMusic.stopMusic(musicRegion, p);
            }
            return null;
        }
    }

    public void regionPlay(Player p, Region musicRegion, List<Region> otherRegions){

        int musicID = musicRegion.getMusicID();
        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(musicID));
        int loop = path.getInt("loop");

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

        for (int i = 0; i < otherRegions.size(); i++) {
            Region region = otherRegions.get(i);

            playMusic.stopMusic(region, p);
        }

    }

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().stream()
              .filter(player -> !Main.getInstance().getPlayerManager().isDeafened(player))
              .forEach(player -> {

                  List<Region> currentRegions = new ArrayList<>();
                  loadRegion.getPolyRegionsMap().forEach((k,v) -> currentRegions.add(polyContainCheck(player, k, v)));
                  loadRegion.getCubeRegionsMap().forEach((k,v) -> currentRegions.add(cubeContainCheck(player, k, v)));
                  currentRegions.removeAll(Collections.singleton(null));

                  if(!currentRegions.isEmpty()){
                      try{
                          Region maxRegion = currentRegions.stream().max(Comparator.comparing(Region::getWeight)).get();
                          currentRegions.remove(maxRegion);
                          regionPlay(player, maxRegion, currentRegions);
                      } catch(NullPointerException e){
                          //No values
                      }
                  }
        });

        serverTime++;
    }
}
