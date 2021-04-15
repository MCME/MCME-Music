package com.mcmiddleearth.mcmemusic.util;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.data.Region;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

public class PlayMusic {

    Logger log;

    private Main main;
    //private RegionCheck regionCheck;

    // int lastMusic = 0; doesn't work like this, needs to be stored individually for each player instead of one global variable.


    public PlayMusic(Main main) {//, RegionCheck regionCheck){
        this.main = main;
        //this.regionCheck = regionCheck;
    }

    public void playMusic(Region musicRegion, Player p){
        int musicID = musicRegion.getMusicID();
        String soundFile = main.getConfig().getString(String.valueOf(musicID));

        //if(musicID != lastMusic) {

            if(soundFile!=null && !soundFile.contains(":")) {
                p.playSound(p.getLocation(), Sound.valueOf(soundFile), 10000, 1);
            } else {
                p.playSound(p.getLocation(), soundFile, 10000, 1);
            }
            p.sendMessage("You are in a region with music ID of " + musicID + "and playing music " + soundFile);
            musicRegion.addListeningPlayer(p);

            /*String lastSound = main.getConfig().getString(String.valueOf(lastMusic));
            try{
                p.stopSound(Sound.valueOf(lastSound));
            }catch (NullPointerException e){
                //happens first time
                lastMusic = musicID;
            }
            lastMusic = musicID;*/
        //}

    }

    public void stopMusic(Region musicRegion, Player p) {
        int musicID = musicRegion.getMusicID();
        String soundFile = main.getConfig().getString(String.valueOf(musicID));
        musicRegion.removeListeningPlayer(p);
        if(soundFile!=null && !soundFile.contains(":")) {
            p.stopSound(Sound.valueOf(soundFile));
        } else {
            p.stopSound(soundFile);
        }
    }
}
