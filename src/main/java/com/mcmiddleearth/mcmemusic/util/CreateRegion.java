package com.mcmiddleearth.mcmemusic.util;


import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.file.JSONFile;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;


import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;


public class CreateRegion {

    Main main;
    JSONFile jsonFile;

    public CreateRegion(Main main, JSONFile jsonFile) {
        this.main = main;
        this.jsonFile = jsonFile;
    }

    public void regionCreate(Player p, String name, int musicID, int weight) throws IOException {

        Logger log = Bukkit.getLogger();

        Region sel = null;
        try {
            sel = Main.getWorldEdit().getSession(p).getSelection(BukkitAdapter.adapt(p.getWorld()));
            log.info("Selection: " + sel);
        } catch (IncompleteRegionException e) {
            e.printStackTrace();
        }

        if (sel != null) {

            String shape = "poly";
            List<String> pointLocations = new ArrayList<>();

            if (sel instanceof Polygonal2DRegion) {
                Polygonal2DRegion polygon = (Polygonal2DRegion) sel;
                int maxY = polygon.getMaximumPoint().getBlockY();
                shape = "poly";

                List<BlockVector2> points = polygon.getPoints();

                for (BlockVector2 point : points) {
                    int x = point.getBlockX();
                    int z = point.getBlockZ();
                    Location loc = new Location(p.getWorld(), x, maxY, z);
                    String stringLoc = loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ();
                    pointLocations.add(stringLoc);
                }
            }
            else if (sel instanceof CuboidRegion) {
                CuboidRegion cube = (CuboidRegion) sel;
                shape = "cube";

                BlockVector3 pos1 = ((CuboidRegion) sel).getPos1();
                BlockVector3 pos2 = ((CuboidRegion) sel).getPos2();

                int x1 = pos1.getBlockX();
                int y1 = pos1.getBlockY();
                int z1 = pos1.getBlockZ();

                Location loc1 = new Location(p.getWorld(), x1, y1, z1);
                String stringLoc1 = loc1.getWorld().getName() + ":" + loc1.getBlockX() + ":" + loc1.getBlockY() + ":" + loc1.getBlockZ();

                int x2 = pos2.getBlockX();
                int y2 = pos2.getBlockY();
                int z2 = pos2.getBlockZ();

                Location loc2 = new Location(p.getWorld(), x2, y2, z2);
                String stringLoc2 = loc2.getWorld().getName() + ":" + loc2.getBlockX() + ":" + loc2.getBlockY() + ":" + loc2.getBlockZ();

                pointLocations.add(stringLoc1);
                pointLocations.add(stringLoc2);

            }

            if(sel instanceof CuboidRegion || sel instanceof Polygonal2DRegion){
                jsonFile.saveJSON(name);
                jsonFile.writeToJSON(name, pointLocations, musicID, weight, shape);
            }

        }
    }
}
