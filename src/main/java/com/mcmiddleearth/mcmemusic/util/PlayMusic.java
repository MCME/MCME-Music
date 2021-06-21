package com.mcmiddleearth.mcmemusic.util;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.data.Region;
import com.mcmiddleearth.mcmemusic.listener.ResourceListener;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;


public class PlayMusic{

    private Main main;

    public PlayMusic(Main main) {
        this.main = main;
    }

    public void playMusic(Region musicRegion, Player p){
        int musicID = musicRegion.getMusicID();
        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(musicID));

        String composer;
        String soundFile = path.getString("file");
        String name = path.getString("name");
        String link = path.getString("link");
        try{
            composer = path.getString("composer");
        }catch(NullPointerException e){
            composer = "Unknown";
        }

        String message = ChatColor.GREEN + "Playing " + ChatColor.ITALIC + name + ChatColor.RESET + ChatColor.GREEN + " by " + ChatColor.ITALIC + composer + ChatColor.RESET +
                ChatColor.GREEN + " [" + ChatColor.GRAY + link + ChatColor.GREEN + "]";

        if(soundFile!=null && !soundFile.contains(":") && ResourceListener.resourceList.contains(p)) {
                p.playSound(p.getLocation(), Sound.valueOf(soundFile), 10000, 1);
                musicRegion.addListeningPlayer(p);
                p.sendMessage(message);
        }else if(soundFile!=null && ResourceListener.resourceList.contains(p)){
                p.playSound(p.getLocation(), soundFile, 10000, 1);
                musicRegion.addListeningPlayer(p);
                p.sendMessage(message);
        }
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
