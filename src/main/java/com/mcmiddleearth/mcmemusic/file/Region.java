package com.mcmiddleearth.mcmemusic.file;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Region {

    private String name;
    private List<String> points;
    private int musicID;

    public Region(String name, List<String> points, int musicID){
        super();
        this.name = name;
        this.points = points;
        this.musicID = musicID;
    }

    public String getName(){
        return name;
    }

    public List<String> getPoints(){
        return points;
    }

    public int getMusicID(){
        return musicID;
    }

}
