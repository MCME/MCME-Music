package com.mcmiddleearth.mcmemusic.util;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.data.Region;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayMusic {

    private Main main;

    public PlayMusic(Main main) {
        this.main = main;
    }

    public void playMusic(Region musicRegion, Player p){
        int musicID = musicRegion.getMusicID();
        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(musicID));

        String soundFile = path.getString("file");
        String name = path.getString("name");
        String link = path.getString("link");

        p.playSound(p.getLocation(), Sound.valueOf(soundFile), 10000, 1);
        p.sendMessage(ChatColor.GREEN + "Playing " + ChatColor.ITALIC + name + ChatColor.RESET + ChatColor.GREEN + " [" + ChatColor.GRAY + link + ChatColor.GREEN + "]");
        musicRegion.addListeningPlayer(p);

    }

    public void stopMusic(Region musicRegion, Player p) {
        int musicID = musicRegion.getMusicID();
        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(musicID));
        String soundFile = path.getString("file");
        musicRegion.removeListeningPlayer(p);
        if(soundFile!=null && !soundFile.contains(":")) {
            p.stopSound(Sound.valueOf(soundFile));
        } else {
            p.stopSound(soundFile);
        }
    }
}

