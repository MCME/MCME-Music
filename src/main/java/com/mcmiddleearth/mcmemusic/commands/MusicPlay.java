package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.concurrent.CompletableFuture;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class MusicPlay {

    private final Main main;

    public MusicPlay(Main main) {
        this.main = main;
    }

    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("play")
                .executes(ctx -> CommandUtil.requirePlayer(
                        ctx,
                        p -> p.sendMessage(Component.text("Make sure to include a song name!", NamedTextColor.RED))))
                .then(Commands.argument("song", StringArgumentType.greedyString())
                        .suggests(this::suggestSongs)
                        .executes(ctx -> CommandUtil.requirePlayer(
                                ctx, p -> play(p, StringArgumentType.getString(ctx, "song")))))
                .build();
    }

    private CompletableFuture<Suggestions> suggestSongs(
            CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        String remaining = builder.getRemainingLowerCase();
        for (String key : main.getConfig().getKeys(false)) {
            String name = main.getConfig().getString(key + ".name");
            if (name != null && name.toLowerCase().startsWith(remaining)) {
                builder.suggest(name);
            }
        }
        return builder.buildFuture();
    }

    private void play(Player p, String songName) {

        if (MusicCommand.playerListening.containsKey(p)) {
            int id = MusicCommand.playerListening.get(p);

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

            MusicCommand.playerListening.put(p, id);

        } catch (NullPointerException e) {
            p.sendMessage(Component.text("That song doesn't exist!", NamedTextColor.RED));
        }
    }
}
