/*
 * Copyright (C) 2020 MCME
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.mcmiddleearth.mcmemusic;

import com.mcmiddleearth.mcmemusic.commands.MusicRegionCommand;
import com.mcmiddleearth.mcmemusic.data.PlayerManager;
import com.mcmiddleearth.mcmemusic.file.JSONFile;
import com.mcmiddleearth.mcmemusic.regionCheck.RegionCheck;
import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.mcmiddleearth.mcmemusic.util.PlayMusic;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class Main extends JavaPlugin {

    public static Main instance;
    public static WorldEditPlugin WEinstance;

    private JSONFile jsonFile = new JSONFile(this);
    private LoadRegion loadRegion = new LoadRegion(this, jsonFile);
    private CreateRegion createRegion = new CreateRegion(this, jsonFile);
    private PlayMusic playMusic = new PlayMusic(this);//, regionChecker);
    private PlayerManager playerManager;
    private BukkitTask regionChecker;

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        playerManager = new PlayerManager();

        try {
            loadRegion.loadRegions();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.getCommand("music").setExecutor(new MusicRegionCommand(createRegion, loadRegion, this));
        regionChecker = new RegionCheck(loadRegion, playMusic, this).runTaskTimer(this,1000,40);
    }

    @Override
    public void onDisable(){
        if(regionChecker!=null) {
            regionChecker.cancel();
        }
        this.saveConfig();
    }

    public static WorldEditPlugin getWorldEdit(){
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if(p instanceof WorldEditPlugin) return (WorldEditPlugin)p;
        return WEinstance;
    }

    public static Main getInstance(){
        return instance;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public PlayMusic getPlayMusic() {
        return playMusic;
    }

    public LoadRegion getLoadRegion() {
        return loadRegion;
    }
}
