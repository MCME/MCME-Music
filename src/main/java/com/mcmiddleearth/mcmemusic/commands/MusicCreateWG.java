package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Permission;
import com.mcmiddleearth.mcmemusic.util.CreateWGRegion;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public final class MusicCreateWG {

    private final CreateWGRegion createWGRegion;

    public MusicCreateWG(CreateWGRegion createWGRegion) {
        this.createWGRegion = createWGRegion;
    }

    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("create-wg")
                .requires(src -> src.getSender().hasPermission(Permission.MANAGE.getNode()))
                .executes(ctx -> CommandUtil.requirePlayer(
                        ctx, p -> p.sendMessage(Component.text("Please provide a name!", NamedTextColor.RED))))
                .then(Commands.argument("name", StringArgumentType.word())
                        .executes(ctx -> CommandUtil.requirePlayer(
                                ctx, p -> createWGRegion.create(p, StringArgumentType.getString(ctx, "name")))))
                .build();
    }
}
