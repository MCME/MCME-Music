package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MusicStop {

    private final Main main;

    public MusicStop(Main main){
        this.main = main;
    }

    public void stop(Player p){
        int id;

        try{
            id = MusicRegionCommand.playerListening.get(p);
        } catch(NullPointerException e){
            p.sendMessage(ChatColor.RED + "You are not listening to any music!");
            return;
        }

        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(id));
        String soundFile = path.getString("file");
        if(soundFile!=null && !soundFile.contains(":")) {
            p.stopSound(Sound.valueOf(soundFile), SoundCategory.VOICE);
        } else {
            p.stopSound(soundFile, SoundCategory.VOICE);
        }

        p.sendMessage(ChatColor.GREEN + "Stopped Music.");
    }

}
