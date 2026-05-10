package com.mcmiddleearth.mcmemusic.util;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.world.World;
import java.util.Optional;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public final class WorldEditSelections {

    private WorldEditSelections() {}

    public static Optional<Region> get(Player player) {
        LocalSession session = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(player));
        World selectionWorld = session.getSelectionWorld();
        try {
            if (selectionWorld == null) throw new IncompleteRegionException();
            return Optional.of(session.getSelection(selectionWorld));
        } catch (IncompleteRegionException e) {
            player.sendMessage(Component.text("Make a WorldEdit selection first.", NamedTextColor.RED));
            return Optional.empty();
        }
    }
}
