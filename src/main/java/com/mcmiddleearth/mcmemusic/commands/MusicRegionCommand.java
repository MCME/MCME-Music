package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.Permission;
import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MusicRegionCommand implements CommandExecutor {

    private final CreateRegion createRegion;
    private final LoadRegion loadRegion;
    private final Main main;

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

                if(args.length == 0 || args[0].equalsIgnoreCase("info")){
                    sender.sendMessage(ChatColor.RED + "Command: /music on|off|create|delete <name> <music ID> <weight>");
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
                else if(args[0].equalsIgnoreCase("play")){
                    if(playerListening.containsKey(p)){
                        int id = playerListening.get(p);

                        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(id));
                        String soundFile = path.getString("file");
                        if(soundFile!=null && !soundFile.contains(":")) {
                            p.stopSound(Sound.valueOf(soundFile));
                        } else {
                            p.stopSound(soundFile);
                        }

                        p.sendMessage(ChatColor.GREEN + "Stopped Music.");
                    }
                    try{
                        ConfigurationSection section = main.getConfig().getConfigurationSection(args[1]);
                        int id = section.getInt("id");

                        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(id));

                        String soundFile = path.getString("file");
                        String name = path.getString("name");
                        String link = path.getString("link");

                        if(soundFile!=null && !soundFile.contains(":")) {
                            p.playSound(p.getLocation(), Sound.valueOf(soundFile), 10000, 1);
                        } else {
                            p.playSound(p.getLocation(), soundFile, 10000, 1);
                        }
                        p.sendMessage(ChatColor.GREEN + "Playing " + ChatColor.ITALIC + name + ChatColor.RESET +
                                ChatColor.GREEN + " [" + ChatColor.GRAY + link + ChatColor.GREEN + "]");

                        playerListening.put(p, id);

                    } catch(NullPointerException e){
                        p.sendMessage(ChatColor.RED + "That song doesn't exist!");
                    }
                    return true;
                }
                else if(args[0].equalsIgnoreCase("stop")){
                    int id = playerListening.get(p);

                    ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(id));
                    String soundFile = path.getString("file");
                    if(soundFile!=null && !soundFile.contains(":")) {
                        p.stopSound(Sound.valueOf(soundFile));
                    } else {
                        p.stopSound(soundFile);
                    }

                    p.sendMessage(ChatColor.GREEN + "Stopped Music.");
                    return true;
                }

                //manager commands
                if(!p.hasPermission(Permission.MANAGE.getNode())) {
                    p.sendMessage("No permission");
                    return true;
                }

                if(args[0].equalsIgnoreCase("create")){
                    try {
                        createRegion.regionCreate(p, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                        loadRegion.getPolyRegionsMap().clear();
                        loadRegion.getCubeRegionsMap().clear();
                        loadRegion.loadRegions();
                        p.sendMessage(ChatColor.GREEN + "Regions have been reloaded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    sender.sendMessage(ChatColor.GREEN + "Region Created.");
                    return true;
                }
                else if(args[0].equalsIgnoreCase("reload")){
                    try {
                        loadRegion.getPolyRegionsMap().clear();
                        loadRegion.getCubeRegionsMap().clear();
                        loadRegion.loadRegions();
                        p.sendMessage(ChatColor.GREEN + "Regions have been reloaded");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
                sender.sendMessage(ChatColor.RED + "Command: /music on|off|create|delete <name> <music ID> <weight>");
            }
        }
        return false;
    }
}
