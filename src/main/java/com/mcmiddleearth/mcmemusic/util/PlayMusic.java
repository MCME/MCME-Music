package com.mcmiddleearth.mcmemusic.util;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.listeners.RegionCheck;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Logger;

public class PlayMusic {

    Logger log;

    private Main main;
    private RegionCheck regionCheck;

    public int lastMusic = 0;

    public String soundFile;
    public String lastSound;

    public PlayMusic(Main main, RegionCheck regionCheck){
        this.main = main;
        this.regionCheck = regionCheck;
    }

    public void playMusic(Integer musicID, Player p){
        soundFile = main.getConfig().getString(String.valueOf(musicID));

        if(musicID != lastMusic) {

            p.playSound(p.getLocation(), Sound.valueOf(soundFile), 10, 1);
            p.sendMessage("You are in a region with music ID of " + musicID + "and playing music " + soundFile);

            lastSound = main.getConfig().getString(String.valueOf(lastMusic));
            try{
                p.stopSound(Sound.valueOf(lastSound));
            }catch (NullPointerException e){
                //happens first time
                lastMusic = musicID;
            }
            lastMusic = musicID;

            new BukkitRunnable() {
                @Override
                public void run () {
                    lastMusic = 0;
                }
            }.runTaskLater(main, 3000);
        }

    }


}
