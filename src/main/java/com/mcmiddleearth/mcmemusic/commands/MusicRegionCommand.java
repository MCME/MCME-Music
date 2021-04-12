package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.Permission;
import com.mcmiddleearth.mcmemusic.data.Region;
import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MusicRegionCommand implements CommandExecutor {

    private CreateRegion createRegion;
    private LoadRegion loadRegion;

    public MusicRegionCommand(CreateRegion createRegion, LoadRegion loadRegion){
        this.createRegion = createRegion;
        this.loadRegion = loadRegion;
    }

    public void containCheck(Player p, Polygonal2DRegion region, Region musicRegion){
        int x, z;
        x = p.getLocation().getBlockX();
        z = p.getLocation().getBlockZ();
        BlockVector3 playerVector = BlockVector3.at(x, 0, z);

        if(region.contains(playerVector)){
            p.sendMessage("You are in a region with music ID of " + musicRegion.getMusicID());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (s.equalsIgnoreCase("music")) {
                //user commands
                if(!p.hasPermission(Permission.LISTEN.getNode())) {
                    p.sendMessage("No permission");
                    return true;
                }
                if(args.length == 0 || args[0].equalsIgnoreCase("info")){
                    sender.sendMessage("Command: /music create|delete <name> <musicID>");
                    return true;
                } else if(args[0].equalsIgnoreCase("off")) {
                    Main.getInstance().getLoadRegion().getRegionsMap().forEach((region,musicRegion)-> {
                        Main.getInstance().getPlayMusic().stopMusic(musicRegion, p);
                    });
                    Main.getInstance().getPlayerManager().deafen(p);
                    p.sendMessage("MCME music disabled.");
                    return true;
                } else if(args[0].equalsIgnoreCase("on")) {
                    Main.getInstance().getPlayerManager().undeafen(p);
                    p.sendMessage("MCME music enabled.");
                    return true;
                }

                //manager commands
                if(!p.hasPermission(Permission.MANAGE.getNode())) {
                    p.sendMessage("No permission");
                    return true;
                }
                if(args[0].equalsIgnoreCase("create")){
                    try {
                        createRegion.regionCreate(p, args[1], Integer.parseInt(args[2]));
                        loadRegion.loadRegions();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(Color.GREEN + "Region Created.");
                    return true;
                }
                else if(args[0].equalsIgnoreCase("test")){
                    loadRegion.getRegionsMap().forEach((k, v) -> containCheck(p, k, v));
                    return true;
                }
                else if(args[0].equalsIgnoreCase("reload")){
                    Main.getInstance().reloadConfig();
                    try {
                        loadRegion.getRegionsMap().clear();
                        loadRegion.loadRegions();
                        p.sendMessage(ChatColor.GREEN + "Regions have been reloaded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            }
        }
        return false;
    }
}
