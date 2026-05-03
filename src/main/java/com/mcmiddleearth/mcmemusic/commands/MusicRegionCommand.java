package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.Permission;
import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.HashMap;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

@SuppressWarnings("UnstableApiUsage")
public class MusicRegionCommand {

    private final CreateRegion createRegion;
    private final LoadRegion loadRegion;
    private final Main main;

    public static HashMap<Player, Integer> playerListening = new HashMap<>();

    public MusicRegionCommand(CreateRegion createRegion, LoadRegion loadRegion, Main main) {
        this.createRegion = createRegion;
        this.loadRegion = loadRegion;
        this.main = main;
    }

    public LiteralCommandNode<CommandSourceStack> node() {
        SuggestionProvider<CommandSourceStack> songSuggestions = (ctx, builder) -> {
            String remaining = builder.getRemainingLowerCase();
            for (String key : main.getConfig().getKeys(false)) {
                String name = main.getConfig().getString(key + ".name");
                if (name != null && name.toLowerCase().startsWith(remaining)) {
                    builder.suggest(name);
                }
            }
            return builder.buildFuture();
        };

        return Commands.literal("music")
                .requires(src -> src.getSender().hasPermission(Permission.LISTEN.getNode()))
                .executes(ctx -> showInfo(ctx.getSource()))
                .then(Commands.literal("info").executes(ctx -> showInfo(ctx.getSource())))
                .then(Commands.literal("on")
                        .executes(ctx -> requirePlayer(ctx, p -> {
                            Main.getInstance().getPlayerManager().undeafen(p);
                            p.sendMessage(Component.text("MCME music enabled.", NamedTextColor.GREEN));
                        })))
                .then(Commands.literal("off")
                        .executes(ctx -> requirePlayer(ctx, p -> {
                            Main.getInstance().getPlayerManager().deafen(p);
                            p.sendMessage(Component.text("MCME music disabled.", NamedTextColor.RED));
                        })))
                .then(Commands.literal("loop")
                        .executes(ctx -> requirePlayer(ctx, p -> {
                            var pm = Main.getInstance().getPlayerManager();
                            if (pm.isLooped(p)) {
                                pm.unloop(p);
                                p.sendMessage(Component.text("MCME music will now not loop.", NamedTextColor.RED));
                            } else {
                                pm.loop(p);
                                p.sendMessage(Component.text("MCME music will now loop.", NamedTextColor.GREEN));
                            }
                        })))
                .then(Commands.literal("stop").executes(ctx -> requirePlayer(ctx, p -> new MusicStop(main).stop(p))))
                .then(Commands.literal("play")
                        .executes(ctx -> requirePlayer(
                                ctx,
                                p -> p.sendMessage(
                                        Component.text("Make sure to include a song name!", NamedTextColor.RED))))
                        .then(Commands.argument("song", StringArgumentType.greedyString())
                                .suggests(songSuggestions)
                                .executes(ctx -> requirePlayer(ctx, p -> {
                                    String song = StringArgumentType.getString(ctx, "song");
                                    new MusicPlay(main).play(p, song);
                                }))))
                .then(Commands.literal("create")
                        .requires(src -> src.getSender().hasPermission(Permission.MANAGE.getNode()))
                        .executes(ctx -> requirePlayer(
                                ctx,
                                p -> p.sendMessage(
                                        Component.text("Please include a name, id, and weight!", NamedTextColor.RED))))
                        .then(Commands.argument("name", StringArgumentType.word())
                                .then(Commands.argument("id", IntegerArgumentType.integer())
                                        .then(Commands.argument("weight", IntegerArgumentType.integer())
                                                .executes(ctx -> requirePlayer(ctx, p -> {
                                                    String name = StringArgumentType.getString(ctx, "name");
                                                    int id = IntegerArgumentType.getInteger(ctx, "id");
                                                    int weight = IntegerArgumentType.getInteger(ctx, "weight");
                                                    try {
                                                        createRegion.regionCreate(p, name, id, weight);
                                                        loadRegion
                                                                .getPolyRegionsMap()
                                                                .clear();
                                                        loadRegion
                                                                .getCubeRegionsMap()
                                                                .clear();
                                                        loadRegion.loadRegions();
                                                        p.sendMessage(Component.text(
                                                                "Region Created.", NamedTextColor.GREEN));
                                                        p.sendMessage(Component.text(
                                                                "Regions have been reloaded", NamedTextColor.GREEN));
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }))))))
                .then(Commands.literal("reload")
                        .requires(src -> src.getSender().hasPermission(Permission.MANAGE.getNode()))
                        .executes(ctx -> requirePlayer(ctx, p -> {
                            try {
                                loadRegion.getPolyRegionsMap().clear();
                                loadRegion.getCubeRegionsMap().clear();
                                loadRegion.loadRegions();
                                main.reloadConfig();
                                p.sendMessage(
                                        Component.text("Regions and Config have been reloaded", NamedTextColor.GREEN));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        })))
                .build();
    }

    private int showInfo(CommandSourceStack src) {
        if (src.getSender().hasPermission(Permission.MANAGE.getNode())) {
            src.getSender()
                    .sendMessage(Component.text("Command: /music on|off|play <song name>", NamedTextColor.RED)
                            .append(Component.text(" OR ", NamedTextColor.GRAY))
                            .append(Component.text("/music create <name> <id> <weight>", NamedTextColor.RED)));
        } else {
            src.getSender().sendMessage(Component.text("Command: /music on|off|play <song name>", NamedTextColor.RED));
        }
        return Command.SINGLE_SUCCESS;
    }

    private int requirePlayer(CommandContext<CommandSourceStack> ctx, Consumer<Player> body) {
        if (ctx.getSource().getSender() instanceof Player p) {
            body.accept(p);
            return Command.SINGLE_SUCCESS;
        }
        ctx.getSource()
                .getSender()
                .sendMessage(Component.text("This command can only be used by a player.", NamedTextColor.RED));
        return 0;
    }
}
