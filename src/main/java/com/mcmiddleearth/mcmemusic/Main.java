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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mcmiddleearth.mcmemusic.commands.MusicRegionCommand;
import com.mcmiddleearth.mcmemusic.file.JSONFile;
import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.math.BlockVector2;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Main extends JavaPlugin {

    public static JavaPlugin instance;
    public static WorldEditPlugin WEinstance;

    JSONFile jsonFile = new JSONFile(this);
    CreateRegion createRegion = new CreateRegion(this, jsonFile);

    @Override
    public void onEnable() {
        instance = this;
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);

        this.getCommand("musicrg").setExecutor(new MusicRegionCommand(createRegion));

        try {
            String temp = jsonFile.readJson("region1.json").toString();
            getLogger().info(temp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDisable(){
       this.saveConfig();
    }

    public static WorldEditPlugin getWorldEdit(){
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if(p instanceof WorldEditPlugin) return (WorldEditPlugin)p;
        return WEinstance;
    }

    public static JavaPlugin getInstance(){
        return instance;
    }
}
