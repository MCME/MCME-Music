package com.mcmiddleearth.mcmemusic.data;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Region {

    private String name;
    private List<String> points;
    private int musicID;

    private Set<Player> listeningPlayers = new HashSet<>();

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
