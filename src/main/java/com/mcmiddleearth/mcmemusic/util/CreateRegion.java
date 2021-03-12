package com.mcmiddleearth.mcmemusic.util;


import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.file.JSONFile;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;

import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


public class CreateRegion{

    Main main;
    JSONFile jsonFile;
    public CreateRegion(Main main, JSONFile jsonFile){
        this.main = main;
        this.jsonFile = jsonFile;
    }

    public void regionCreate(Player p, String name, int musicID) throws IOException {

        Logger log = Bukkit.getLogger();

        Region sel = null;
        try {
            sel = Main.getWorldEdit().getSession(p).getSelection(BukkitAdapter.adapt(p.getWorld()));
            log.info("Selection: " + sel);
        } catch (IncompleteRegionException e) {
            e.printStackTrace();
        }

        if(sel != null) {
            if (sel instanceof Polygonal2DRegion) {
                Polygonal2DRegion polygon = (Polygonal2DRegion) sel;
                int maxY = polygon.getMaximumPoint().getBlockY();

                List<BlockVector2> points = polygon.getPoints();
                List<String> pointLocations = new ArrayList<>();

                for(BlockVector2 point : points) {
                    int x = point.getBlockX();
                    int z = point.getBlockZ();
                    Location loc = new Location(p.getWorld(), x, maxY, z);
                    String stringLoc = loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
                    pointLocations.add(stringLoc);
                }

                jsonFile.saveJSON(name);
                jsonFile.writeToJSON(name, pointLocations, musicID);
            }
        }
    }
}
