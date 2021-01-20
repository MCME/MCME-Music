package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MusicRegionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (s.equalsIgnoreCase("musicrg")) {
                if (args.length == 0 || args[0].equalsIgnoreCase("info")) {
                    //show info for command
                } else if (args[0].equalsIgnoreCase("create")) {
                    CreateRegion.regionCreate(p);
                    sender.sendMessage("Created region.");
                } else if (args[0].equalsIgnoreCase("delete")) {
                    //delete region
                    sender.sendMessage("Deleted region.");
                }
            }
        }
        else {
            sender.sendMessage("This command must be sent by a player!");
        }
        return false;
    }
}
