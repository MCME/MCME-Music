package com.mcmiddleearth.mcmemusic.util;

import com.google.gson.*;
import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.file.JSONFile;
import com.mcmiddleearth.mcmemusic.data.Region;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector2;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class LoadRegion {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Main main;
    private JSONFile jsonFile;
    private Logger log = Bukkit.getLogger();
    private HashMap<Polygonal2DRegion, Region> allRegions = new HashMap<>();

    public LoadRegion(Main main, JSONFile jsonFile){
        this.main = main;
        this.jsonFile = jsonFile;
    }

    public void loadRegions() throws Exception {
        File dataFolder = new File(String.valueOf(main.getDataFolder()));
        File[] files = dataFolder.listFiles();

        for(File f: files){
            if(f.getName().contains(".json")){
                ArrayList<String> pointList = new ArrayList<String>();
                JsonObject json = (JsonObject) jsonFile.readJson(f.getName());
                JsonPrimitive jsonID = json.getAsJsonPrimitive("musicID");
                String name = json.get("name").getAsString();

                //Get points
                JsonArray points = json.getAsJsonArray("points");

                if(points != null){
                    for(int i=0;i<points.size();i++){
                        pointList.add(String.valueOf(points.get(i)));
                    }
                }

                //Get musicID
                int musicID = jsonID.getAsInt();

                //Log
                log.info("Test log");
                log.info("File:" + f.getName());
                log.info("Points:" + pointList);
                log.info("Music ID:" + musicID);

                //Create Region
                Polygonal2DRegion region = new Polygonal2DRegion();

                for(int i=0;i<pointList.size();i++){
                    //Add point region
                    int x;
                    int z;

                    //Deserialize
                    int firstIndex = pointList.get(i).indexOf(":");
                    int lastIndex = pointList.get(i).lastIndexOf(":");
                    int secondIndex = pointList.get(i).indexOf(":", firstIndex + 1);
                    String world = pointList.get(i).substring(1, firstIndex);
                    String stringX = pointList.get(i).substring(firstIndex+1, secondIndex);
                    String stringZ = pointList.get(i).substring(lastIndex+1, pointList.get(i).length()-1);

                    log.info(world);

                    //Set variables to final values
                    x = Integer.parseInt(stringX);
                    z = Integer.parseInt(stringZ);
                    World bukkitWorld = Bukkit.getServer().getWorld(world);
                    com.sk89q.worldedit.world.World regionWorld = BukkitAdapter.adapt(bukkitWorld);
                    BlockVector2 pointLoc = BlockVector2.at(x,z);

                    //Add Point and Set World
                    region.setWorld(regionWorld);
                    region.addPoint(pointLoc);
                }

                allRegions.put(region, new Region(name, pointList, musicID));

            }
        }
        log.info(String.valueOf(allRegions));
    }

    public HashMap<Polygonal2DRegion, Region> getRegionsMap(){
        return allRegions;
    }

}
