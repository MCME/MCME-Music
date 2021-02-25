package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MusicRegionCommand implements CommandExecutor {

    private CreateRegion createRegion;

    public MusicRegionCommand(CreateRegion createRegion){
        this.createRegion = createRegion;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (s.equalsIgnoreCase("musicrg")) {
                if(args.length == 0 || args[0].equalsIgnoreCase("info")){
                    sender.sendMessage("Command: /musicrg create|delete <name> <musicID>");
                }
                else if(args[0].equalsIgnoreCase("create")){
                    try {
                        createRegion.regionCreate(p, args[1], Integer.parseInt(args[2]));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(Color.GREEN + "Region Created.");
                }
            }
        }
        return false;
    }
}
