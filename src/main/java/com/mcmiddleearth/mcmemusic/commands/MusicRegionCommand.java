package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.Permission;
import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import java.util.HashMap;

public class MusicRegionCommand implements CommandExecutor {

    private final CreateRegion createRegion;
    private final LoadRegion loadRegion;
    private final Main main;

    public static HashMap<Player, Integer> playerListening = new HashMap<>();

    public MusicRegionCommand(CreateRegion createRegion, LoadRegion loadRegion, Main main){
        this.createRegion = createRegion;
        this.loadRegion = loadRegion;
        this.main = main;
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

                if(p.hasPermission(Permission.MANAGE.getNode()) && (args.length == 0 || args[0].equalsIgnoreCase("info"))){
                    sender.sendMessage(ChatColor.RED + "Command: /music on|off|play <song name>" + ChatColor.GRAY +  " OR " + ChatColor.RED + "/music create <name> <id> <weight> <rp>");
                    return true;
                }

                if(p.hasPermission(Permission.LISTEN.getNode()) && (args.length == 0 || args[0].equalsIgnoreCase("info"))){
                    sender.sendMessage(ChatColor.RED + "Command: /music on|off|play <song name>");
                    return true;
                }

                else if(args[0].equalsIgnoreCase("off")) {
                    Main.getInstance().getPlayerManager().deafen(p);
                    p.sendMessage(ChatColor.RED + "MCME music disabled.");
                    return true;
                }
                else if(args[0].equalsIgnoreCase("on")) {
                    Main.getInstance().getPlayerManager().undeafen(p);
                    p.sendMessage(ChatColor.GREEN + "MCME music enabled.");
                    return true;
                }
                else if(args[0].equalsIgnoreCase("loop")){
                    if(Main.getInstance().getPlayerManager().isLooped(p)){
                        Main.getInstance().getPlayerManager().unloop(p);
                        p.sendMessage(ChatColor.RED + "MCME music will now not loop.");
                    }else{
                        Main.getInstance().getPlayerManager().loop(p);
                        p.sendMessage(ChatColor.GREEN + "MCME music will now loop.");
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("play")){
                    //MusicPlay instanced here b/c it would not work in constructor
                    MusicPlay musicPlay = new MusicPlay(main);
                    musicPlay.play(p, args);
                    return true;
                }
                else if(args[0].equalsIgnoreCase("stop")){
                    //MusicStop instanced here b/c it would not work in constructor
                    MusicStop musicStop = new MusicStop(main);
                    musicStop.stop(p);
                    return true;
                }

                //manager commands
                if(!p.hasPermission(Permission.MANAGE.getNode())) {
                    p.sendMessage("No permission");
                    return true;
                }

                if(args[0].equalsIgnoreCase("create")){
                    try {
                        if(args.length == 5) {
                            createRegion.regionCreate(p, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]), args[4]);
                            loadRegion.getPolyRegionsMap().clear();
                            loadRegion.getCubeRegionsMap().clear();
                            loadRegion.loadRegions();
                            p.sendMessage(ChatColor.GREEN + "Region Created.");
                            p.sendMessage(ChatColor.GREEN + "Regions have been reloaded");
                        }else{
                            p.sendMessage(ChatColor.RED + "Please include a name, id, weight, and rp!");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("reload")){
                    try {
                        loadRegion.getPolyRegionsMap().clear();
                        loadRegion.getCubeRegionsMap().clear();
                        loadRegion.loadRegions();
                        main.reloadConfig();
                        p.sendMessage(ChatColor.GREEN + "Regions and Config have been reloaded");
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