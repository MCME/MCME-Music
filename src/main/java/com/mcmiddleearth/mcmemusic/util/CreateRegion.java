package com.mcmiddleearth.mcmemusic.util;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.file.JSONFile;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import java.io.IOException;
import java.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class CreateRegion {

    Main main;
    JSONFile jsonFile;

    public CreateRegion(Main main, JSONFile jsonFile) {
        this.main = main;
        this.jsonFile = jsonFile;
    }

    public void regionCreate(Player p, String name, int musicID, int weight) throws IOException {
        Optional<Region> maybeSel = WorldEditSelections.get(p);
        if (maybeSel.isEmpty()) return;

        Region sel = maybeSel.get();
        main.getLogger().info("Selection: " + sel);

        String shape;
        List<String> pointLocations = new ArrayList<>();

        if (sel instanceof Polygonal2DRegion polygon) {
            int maxY = polygon.getMaximumPoint().y();
            shape = "poly";

            List<BlockVector2> points = polygon.getPoints();

            for (BlockVector2 point : points) {
                int x = point.x();
                int z = point.z();
                Location loc = new Location(p.getWorld(), x, maxY, z);
                String stringLoc = loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":"
                        + loc.getBlockZ();
                pointLocations.add(stringLoc);
            }
        } else if (sel instanceof CuboidRegion cuboid) {
            shape = "cube";

            BlockVector3 pos1 = cuboid.getPos1();
            BlockVector3 pos2 = cuboid.getPos2();

            int x1 = pos1.x();
            int y1 = pos1.y();
            int z1 = pos1.z();

            Location loc1 = new Location(p.getWorld(), x1, y1, z1);
            String stringLoc1 = loc1.getWorld().getName() + ":" + loc1.getBlockX() + ":" + loc1.getBlockY() + ":"
                    + loc1.getBlockZ();

            int x2 = pos2.x();
            int y2 = pos2.y();
            int z2 = pos2.z();

            Location loc2 = new Location(p.getWorld(), x2, y2, z2);
            String stringLoc2 = loc2.getWorld().getName() + ":" + loc2.getBlockX() + ":" + loc2.getBlockY() + ":"
                    + loc2.getBlockZ();

            pointLocations.add(stringLoc1);
            pointLocations.add(stringLoc2);
        } else {
            p.sendMessage(Component.text("Selection must be cuboid or polygonal.", NamedTextColor.RED));
            return;
        }

        jsonFile.saveJSON(name);
        jsonFile.writeToJSON(name, pointLocations, musicID, weight, shape);
    }
}
