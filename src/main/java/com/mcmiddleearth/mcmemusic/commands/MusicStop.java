package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MusicStop {

    private final Main main;

    public MusicStop(Main main) {
        this.main = main;
    }

    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("stop")
                .executes(ctx -> CommandUtil.requirePlayer(ctx, this::stop))
                .build();
    }

    private void stop(Player p) {
        int id;

        try {
            id = MusicCommand.playerListening.get(p);
        } catch (NullPointerException e) {
            p.sendMessage(Component.text("You are not listening to any music!", NamedTextColor.RED));
            return;
        }

        ConfigurationSection path = main.getConfig().getConfigurationSection(String.valueOf(id));
        String soundFile = path.getString("file");
        if (soundFile != null && !soundFile.contains(":")) {
            p.stopSound(Sound.valueOf(soundFile), SoundCategory.VOICE);
        } else {
            p.stopSound(soundFile, SoundCategory.VOICE);
        }

        p.sendMessage(Component.text("Stopped Music.", NamedTextColor.GREEN));
    }
}
