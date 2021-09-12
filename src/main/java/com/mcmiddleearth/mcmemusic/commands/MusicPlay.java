package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class MusicPlay {

    private Main main;

    public MusicPlay(Main main){
        this.main = main;
    }

        public void play(Player p, String[] args){

        if(args.length < 2){
            p.sendMessage(ChatColor.RED + "Make sure to include a song name!");
            return;
        }

        if(MusicRegionCommand.playerListening.containsKey(p)){
            int id = MusicRegionCommand.playerListening.get(p);

            ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(id));
            String soundFile = path.getString("file");
            if(soundFile!=null && !soundFile.contains(":")) {
                p.stopSound(Sound.valueOf(soundFile), SoundCategory.VOICE);
            } else {
                p.stopSound(soundFile, SoundCategory.VOICE);
            }

            p.sendMessage(ChatColor.GREEN + "Stopped Music.");
        }

        try{
            StringBuilder sb = new StringBuilder();
            for(int i = 1; i < args.length; i++) {
                sb.append(args[i]);
                sb.append(" ");
            }

            String command = sb.toString();
            command = command.substring(0, command.length() - 1);

            int id = 0;

            for(String key : main.getConfig().getConfigurationSection("").getKeys(false)){
                if(main.getConfig().getString(key + ".name").equalsIgnoreCase(command)){
                    id = Integer.parseInt(key);
                }
            }

            if(id == 0){
                p.sendMessage(ChatColor.RED + "That song doesn't exist");
                return;
            }

            ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(id));

            String composer;
            String soundFile = path.getString("file");
            String name = path.getString("name");
            String link = path.getString("link");
            try{
                composer = path.getString("composer");
            }catch(NullPointerException e){
                composer = "Unknown";
            }

            if(soundFile!=null && !soundFile.contains(":")) {
                p.playSound(p.getLocation(), Sound.valueOf(soundFile), SoundCategory.VOICE, 1, 1);
            } else {
                p.playSound(p.getLocation(), soundFile, SoundCategory.VOICE,1, 1);
            }

            p.sendMessage(ChatColor.GREEN + "Playing " + ChatColor.ITALIC + name + ChatColor.RESET + ChatColor.GREEN + " by " +
                    ChatColor.ITALIC + composer + ChatColor.RESET + ChatColor.GREEN + " [" + ChatColor.GRAY + link + ChatColor.GREEN + "]");

            MusicRegionCommand.playerListening.put(p, id);

        } catch(NullPointerException e){
            p.sendMessage(ChatColor.RED + "That song doesn't exist!");
        }
    }

}
