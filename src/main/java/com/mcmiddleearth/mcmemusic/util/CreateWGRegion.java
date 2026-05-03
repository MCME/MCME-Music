package com.mcmiddleearth.mcmemusic.util;

import com.mcmiddleearth.mcmemusic.Main;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class CreateWGRegion {

    private final Main main;

    public CreateWGRegion(Main main) {
        this.main = main;
    }

    public void create(Player p, String name) {
        Region sel;
        try {
            sel = Main.getWorldEdit().getSession(p).getSelection(BukkitAdapter.adapt(p.getWorld()));
        } catch (IncompleteRegionException e) {
            p.sendMessage(Component.text("Make a WorldEdit selection first.", NamedTextColor.RED));
            return;
        }

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
        RegionManager rm = container.get(BukkitAdapter.adapt(p.getWorld()));
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
