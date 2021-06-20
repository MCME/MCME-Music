package com.mcmiddleearth.mcmemusic.data;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Region {

    private final String name;
    private final List<String> points;
    private final int musicID;
    private final int weight;
    private final String shape;

    private final Set<Player> listeningPlayers = new HashSet<>();

    public Region(String name, List<String> points, int musicID, int weight, String shape){
        super();
        this.name = name;
        this.points = points;
        this.musicID = musicID;
        this.weight = weight;
        this.shape = shape;
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

    public int getWeight(){
        return weight;
    }

    public String getShape(){
        return shape;
    }

    public boolean isListening(Player player) {
        return listeningPlayers.contains(player);
    }

    public void addListeningPlayer(Player player) {
        listeningPlayers.add(player);
    }

    public void removeListeningPlayer(Player player) {
        listeningPlayers.remove(player);
    }
}

