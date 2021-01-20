package com.mcmiddleearth.mcmemusic.util;

import com.google.common.collect.Iterables;
import com.mcmiddleearth.mcmemusic.Main;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;


import java.util.List;


public class CreateRegion{

    public static void regionCreate(Player p) {

        FileConfiguration config = Main.getInstance().getConfig();
        Main.getInstance().saveDefaultConfig();

        ConfigurationSection newSection = null;
        if(config.getKeys(false).isEmpty()){
            newSection = config.createSection("0");
            
        } else {
            ConfigurationSection lastSec = config.getConfigurationSection(Iterables.getLast(config.getKeys(false)));
            int num = Integer.parseInt(lastSec.getName());
            newSection = config.createSection(String.valueOf(num+1));
        }

        Region sel = null;
        try {
            sel = Main.getWorldEdit().getSession(p).getSelection(BukkitAdapter.adapt(p.getWorld()));
        } catch (IncompleteRegionException e) {
            e.printStackTrace();
        }

        if (sel != null) {
            if (sel instanceof Polygonal2DRegion) {
                Polygonal2DRegion polygon = (Polygonal2DRegion) sel;

                List<BlockVector2> points = polygon.getPoints();

                config.set(String.valueOf(newSection), points);
                Main.getInstance().saveDefaultConfig();
            }
        }
    }
}
