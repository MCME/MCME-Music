package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class MusicRegionCommand implements CommandExecutor {

    private CreateRegion createRegion;
    private LoadRegion loadRegion;

    public MusicRegionCommand(CreateRegion createRegion, LoadRegion loadRegion){
        this.createRegion = createRegion;
        this.loadRegion = loadRegion;
    }

    public void containCheck(Player p, Polygonal2DRegion region, Integer musicID){
        int x, z;
        x = p.getLocation().getBlockX();
        z = p.getLocation().getBlockZ();
        BlockVector3 playerVector = BlockVector3.at(x, 0, z);

        if(region.contains(playerVector)){
            p.sendMessage("You are in a region with music ID of " + musicID);
        }
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
                else if(args[0].equalsIgnoreCase("test")){
                    loadRegion.allRegions.forEach((k, v) -> containCheck(p, k, v));
                }
                else if(args[0].equalsIgnoreCase("reload")){
                    try {
                        loadRegion.getRegionsMap().clear();
                        loadRegion.loadRegions();
                        p.sendMessage(ChatColor.GREEN + "Regions have been reloaded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }
}
