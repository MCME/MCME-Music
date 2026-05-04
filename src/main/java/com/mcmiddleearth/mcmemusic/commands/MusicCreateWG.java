package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Permission;
import com.mcmiddleearth.mcmemusic.util.WorldEditSelections;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedPolygonalRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public final class MusicCreateWG {

    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("create-wg")
                .requires(src -> src.getSender().hasPermission(Permission.MANAGE.getNode()))
                .executes(ctx -> CommandUtil.requirePlayer(
                        ctx, p -> p.sendMessage(Component.text("Please provide a name!", NamedTextColor.RED))))
                .then(Commands.argument("name", StringArgumentType.word())
                        .executes(ctx -> CommandUtil.requirePlayer(
                                ctx, p -> create(p, StringArgumentType.getString(ctx, "name")))))
                .build();
    }

    private void create(Player p, String name) {
        Optional<Region> maybeSel = WorldEditSelections.get(p);
        if (maybeSel.isEmpty()) return;
        Region sel = maybeSel.get();

        ProtectedRegion region;
        if (sel instanceof CuboidRegion cube) {
            region = new ProtectedCuboidRegion(name, cube.getMinimumPoint(), cube.getMaximumPoint());
        } else if (sel instanceof Polygonal2DRegion poly) {
            region = new ProtectedPolygonalRegion(
                    name,
                    poly.getPoints(),
                    poly.getMinimumPoint().y(),
                    poly.getMaximumPoint().y());
        } else {
            p.sendMessage(Component.text("Selection must be cuboid or polygonal.", NamedTextColor.RED));
            return;
        }

        region.setFlag(Flags.BUILD, StateFlag.State.ALLOW);

        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager rm = container.get(sel.getWorld());
        if (rm == null) {
            p.sendMessage(Component.text("WorldGuard has no region manager for this world.", NamedTextColor.RED));
            return;
        }
        if (rm.hasRegion(name)) {
            p.sendMessage(Component.text("Region '" + name + "' already exists.", NamedTextColor.RED));
            return;
        }
        rm.addRegion(region);

        p.sendMessage(Component.text("Created WG region '" + name + "' with build=allow.", NamedTextColor.GREEN));
    }
}
