package com.mcmiddleearth.mcmemusic.util;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.listeners.RegionCheck;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayMusic {

    Main main;
    boolean musicPlaying = false;

    public PlayMusic(Main main){
        this.main = main;
    }

    public void playMusic(Integer musicID, Player p){
        String soundFile = main.getConfig().getString(String.valueOf(musicID));
        if(!musicPlaying) {
            p.playSound(p.getLocation(), Sound.valueOf(soundFile), 10, 1);
            musicPlaying = true;
            new BukkitRunnable() {
                @Override
                public void run () {
                    musicPlaying = false;
                    p.sendMessage("Stopped Music");
                }
            }.runTaskLater(main, 3000);
        }

    }


}
