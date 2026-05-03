package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MusicPlay {

    private Main main;

    public MusicPlay(Main main) {
        this.main = main;
    }

    public void play(Player p, String songName) {

        if (MusicRegionCommand.playerListening.containsKey(p)) {
            int id = MusicRegionCommand.playerListening.get(p);

            ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(id));
            String soundFile = path.getString("file");
            if (soundFile != null && !soundFile.contains(":")) {
                p.stopSound(Sound.valueOf(soundFile), SoundCategory.VOICE);
            } else {
                p.stopSound(soundFile, SoundCategory.VOICE);
            }

            p.sendMessage(Component.text("Stopped Music.", NamedTextColor.GREEN));
        }

        try {
            String command = songName;

            int id = 0;

            for (String key : main.getConfig().getConfigurationSection("").getKeys(false)) {
                if (main.getConfig().getString(key + ".name").equalsIgnoreCase(command)) {
                    id = Integer.parseInt(key);
                }
            }

            if (id == 0) {
                p.sendMessage(Component.text("That song doesn't exist", NamedTextColor.RED));
                return;
            }

            ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(id));

            String composer;
            String soundFile = path.getString("file");
            String name = path.getString("name");
            String link = path.getString("link");
            try {
                composer = path.getString("composer");
            } catch (NullPointerException e) {
                composer = "Unknown";
            }

            if (soundFile != null && !soundFile.contains(":")) {
                p.playSound(p.getLocation(), Sound.valueOf(soundFile), SoundCategory.VOICE, 1, 1);
            } else {
                p.playSound(p.getLocation(), soundFile, SoundCategory.VOICE, 1, 1);
            }

            p.sendMessage(Component.text("Playing ", NamedTextColor.GREEN)
                    .append(Component.text(name, NamedTextColor.GREEN).decorate(TextDecoration.ITALIC))
                    .append(Component.text(" by ", NamedTextColor.GREEN))
                    .append(Component.text(composer, NamedTextColor.GREEN).decorate(TextDecoration.ITALIC))
                    .append(Component.text(" [", NamedTextColor.GREEN))
                    .append(Component.text(link, NamedTextColor.GRAY))
                    .append(Component.text("]", NamedTextColor.GREEN)));

            MusicRegionCommand.playerListening.put(p, id);

        } catch (NullPointerException e) {
            p.sendMessage(Component.text("That song doesn't exist!", NamedTextColor.RED));
        }
    }
}
