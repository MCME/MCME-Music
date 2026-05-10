package com.mcmiddleearth.mcmemusic.util;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.data.Region;
import com.mcmiddleearth.mcmemusic.listener.ResourceListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class PlayMusic {

    private Main main;

    public PlayMusic(Main main) {
        this.main = main;
    }

    public void playMusic(Region musicRegion, Player p) {
        int musicID = musicRegion.getMusicID();
        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(musicID));

        String composer;
        String soundFile = path.getString("file");
        String name = path.getString("name");
        String link = path.getString("link");
        try {
            composer = path.getString("composer");
        } catch (NullPointerException e) {
            composer = "Unknown";
        }

        Component message = Component.text("Playing ", NamedTextColor.GREEN)
                .append(Component.text(name, NamedTextColor.GREEN).decorate(TextDecoration.ITALIC))
                .append(Component.text(" by ", NamedTextColor.GREEN))
                .append(Component.text(composer, NamedTextColor.GREEN).decorate(TextDecoration.ITALIC))
                .append(Component.text(" [", NamedTextColor.GREEN))
                .append(Component.text(link, NamedTextColor.GRAY))
                .append(Component.text("]", NamedTextColor.GREEN));

        musicRegion.addListeningPlayer(p);

        if (soundFile != null && !soundFile.contains(":") && ResourceListener.resourceList.contains(p)) {
            p.playSound(p.getLocation(), Sound.valueOf(soundFile), SoundCategory.VOICE, 1, 1);
            p.sendMessage(message);
        } else if (soundFile != null && ResourceListener.resourceList.contains(p)) {
            p.playSound(p.getLocation(), soundFile, SoundCategory.VOICE, 1, 1);
            p.sendMessage(message);
        }
    }

    public void stopMusic(Region musicRegion, Player p) {
        int musicID = musicRegion.getMusicID();
        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(musicID));
        String soundFile = path.getString("file");
        if (soundFile != null && !soundFile.contains(":")) {
            p.stopSound(Sound.valueOf(soundFile), SoundCategory.VOICE);
        } else {
            p.stopSound(soundFile, SoundCategory.VOICE);
        }
        musicRegion.removeListeningPlayer(p);
    }
}
