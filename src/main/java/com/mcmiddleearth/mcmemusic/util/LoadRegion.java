package com.mcmiddleearth.mcmemusic.util;

import com.google.gson.*;
import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.file.JSONFile;
import com.mcmiddleearth.mcmemusic.data.Region;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class LoadRegion {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Main main;
    private final JSONFile jsonFile;
    private final Logger log = Bukkit.getLogger();
    private final HashMap<Polygonal2DRegion, Region> polyRegions = new HashMap<>();
    private final HashMap<CuboidRegion, Region> cubeRegions = new HashMap<>();

    public LoadRegion(Main main, JSONFile jsonFile){
        this.main = main;
        this.jsonFile = jsonFile;
    }

    public void loadRegions() throws Exception {
        File dataFolder = new File(String.valueOf(main.getDataFolder()));
        File[] files = dataFolder.listFiles();

        for(File f: files) {
                JsonObject json;
                try{
                    json = (JsonObject) jsonFile.readJson(f.getName());
                }catch(com.google.gson.JsonSyntaxException | java.lang.ClassCastException e){
                    continue;
                }
                ArrayList<String> pointList = new ArrayList<>();
                JsonPrimitive jsonID = json.getAsJsonPrimitive("musicID");
                String name = json.get("name").getAsString();
                String shape;

                try {
                    shape = json.get("shape").getAsString();
                } catch (NullPointerException e) {
                    shape = "poly";
                }

                int musicID = jsonID.getAsInt();
                int weight = jsonID.getAsInt();

                JsonArray points = json.getAsJsonArray("points");
                if (points != null) {
                    for (int i = 0; i < points.size(); i++) {
                        pointList.add(String.valueOf(points.get(i)));
                    }
                }

                if (shape.contains("poly")) {
                    polyLoad(name, pointList, musicID, weight, shape);

                } else if (shape.contains("cube")) {
                    cubeLoad(name, pointList, musicID, weight, shape);

                }
        }
    }

    public void polyLoad(String name, ArrayList<String> pointList, int musicID, int weight, String shape){
        Polygonal2DRegion region = new Polygonal2DRegion();

        for (String s : pointList) {
            int firstIndex = s.indexOf(":");
            int lastIndex = s.lastIndexOf(":");
            int secondIndex = s.indexOf(":", firstIndex + 1);
            String world = s.substring(1, firstIndex);
            String stringX = s.substring(firstIndex + 1, secondIndex);
            String stringZ = s.substring(lastIndex + 1, s.length() - 1);

            int x = Integer.parseInt(stringX);
            int z = Integer.parseInt(stringZ);
            World bukkitWorld = Bukkit.getServer().getWorld(world);
            com.sk89q.worldedit.world.World regionWorld = BukkitAdapter.adapt(bukkitWorld);
            BlockVector2 pointLoc = BlockVector2.at(x, z);

            region.setWorld(regionWorld);
            region.addPoint(pointLoc);
        }
        polyRegions.put(region, new Region(name, pointList, musicID, weight, shape));
    }

    public void cubeLoad(String name, ArrayList<String> pointList, int musicID, int weight, String shape){
        List<BlockVector3> positions = new ArrayList<>();
        String world = null;

        for (String s : pointList) {
            int firstIndex = s.indexOf(":");
            int lastIndex = s.lastIndexOf(":");
            int secondIndex = s.indexOf(":", firstIndex + 1);
            world = s.substring(1, firstIndex);
            String stringX = s.substring(firstIndex + 1, secondIndex);
            String stringY = s.substring(secondIndex + 1, lastIndex);
            String stringZ = s.substring(lastIndex + 1, pointList.get(1).length() - 1);

            int x = Integer.parseInt(stringX);
            int y = Integer.parseInt(stringY);
            int z = Integer.parseInt(stringZ);

            BlockVector3 pos = BlockVector3.at(x, y, z);
            positions.add(pos);
        }
        CuboidRegion region = new CuboidRegion(positions.get(0), positions.get(1));

        World bukkitWorld = Bukkit.getServer().getWorld(world);
        com.sk89q.worldedit.world.World regionWorld = BukkitAdapter.adapt(bukkitWorld);
        region.setWorld(regionWorld);

        cubeRegions.put(region, new Region(name, pointList, musicID, weight, shape));
    }

    public HashMap<Polygonal2DRegion, Region> getPolyRegionsMap(){
        return polyRegions;
    }
    public HashMap<CuboidRegion, Region> getCubeRegionsMap(){
        return cubeRegions;
    }

}
