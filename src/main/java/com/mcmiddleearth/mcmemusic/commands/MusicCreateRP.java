package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Permission;
import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.mcmiddleearth.mcmemusic.util.SelectionProximity;
import com.mcmiddleearth.mcmemusic.util.WorldEditSelections;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.sk89q.worldedit.regions.Region;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public final class MusicCreateRP {

    private final CreateRegion createRegion;
    private final LoadRegion loadRegion;

    public MusicCreateRP(CreateRegion createRegion, LoadRegion loadRegion) {
        this.createRegion = createRegion;
        this.loadRegion = loadRegion;
    }

    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("create-rp")
                .requires(src -> src.getSender().hasPermission(Permission.MANAGE.getNode()))
                .executes(ctx -> CommandUtil.requirePlayer(
                        ctx,
                        p -> p.sendMessage(
                                Component.text("Please include a name, id, and weight!", NamedTextColor.RED))))
                .then(Commands.argument("name", StringArgumentType.word())
                        .then(Commands.argument("id", IntegerArgumentType.integer())
                                .then(Commands.argument("weight", IntegerArgumentType.integer())
                                        .executes(ctx -> CommandUtil.requirePlayer(
                                                ctx,
                                                p -> create(
                                                        p,
                                                        StringArgumentType.getString(ctx, "name"),
                                                        IntegerArgumentType.getInteger(ctx, "id"),
                                                        IntegerArgumentType.getInteger(ctx, "weight"),
                                                        false,
                                                        ctx.getInput())))
                                        .then(Commands.literal("--ignore-proximity")
                                                .executes(ctx -> CommandUtil.requirePlayer(
                                                        ctx,
                                                        p -> create(
                                                                p,
                                                                StringArgumentType.getString(ctx, "name"),
                                                                IntegerArgumentType.getInteger(ctx, "id"),
                                                                IntegerArgumentType.getInteger(ctx, "weight"),
                                                                true,
                                                                ctx.getInput())))))))
                .build();
    }

    private void create(Player p, String name, int id, int weight, boolean ignoreProximity, String input) {
        if (!ignoreProximity) {
            Optional<Region> maybeSel = WorldEditSelections.get(p);
            if (maybeSel.isEmpty()) return;

            SelectionProximity.Result proximity = SelectionProximity.check(p, maybeSel.get());
            if (!proximity.isOk()) {
                p.sendMessage(SelectionProximity.describe(proximity)
                        .append(Component.text(" "))
                        .append(Component.text("[Use anyway]", NamedTextColor.YELLOW)
                                .clickEvent(ClickEvent.suggestCommand("/" + input + " --ignore-proximity"))));
                return;
            }
        }

        try {
            createRegion.regionCreate(p, name, id, weight);
            loadRegion.loadRegions();
            p.sendMessage(Component.text("Region Created.", NamedTextColor.GREEN));
            p.sendMessage(Component.text("Regions have been reloaded", NamedTextColor.GREEN));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
