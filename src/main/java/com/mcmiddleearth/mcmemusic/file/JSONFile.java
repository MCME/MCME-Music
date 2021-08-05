package com.mcmiddleearth.mcmemusic.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.data.Region;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class JSONFile{

    Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().create();

    private final Main main;
    List<String> pointLocations = new ArrayList<>();

    public JSONFile(Main main){
        this.main = main;
    }

    public void saveJSON(String name) throws IOException {
        File root = new File(main.getDataFolder() + File.separator + name + ".json");
    }

    public void writeToJSON(String name, List<String> pointLocations, int musicID, int weight, String shape) throws IOException {
        Region region = new Region(name, pointLocations, musicID, weight, shape);
        File root = new File(main.getDataFolder() + File.separator + name + ".json");
        FileWriter fileWriter = new FileWriter(root);
        gson.toJson(region, fileWriter);
        fileWriter.close();
    }

    public Object readJson(String filename) throws Exception{
        FileReader reader = new FileReader(main.getDataFolder() + File.separator + filename);
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(reader);
    }
}
