package com.mcmiddleearth.mcmemusic.util;

import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class SelectionProximity {

    private static final int THRESHOLD_BLOCKS = 85;

    public enum Verdict {
        OK,
        DIFFERENT_WORLD,
        TOO_FAR
    }

    public record Result(Verdict verdict, double distance, String selectionWorld, String playerWorld) {
        public boolean isOk() {
            return verdict == Verdict.OK;
        }
    }

    private SelectionProximity() {}

    public static Result check(Player player, Region selection) {
        String playerWorld = player.getWorld().getName();
        String selectionWorld =
                selection.getWorld() != null ? selection.getWorld().getName() : "<unknown>";

        if (!playerWorld.equals(selectionWorld)) {
            return new Result(Verdict.DIFFERENT_WORLD, 0, selectionWorld, playerWorld);
        }

        Location loc = player.getLocation();
        BlockVector3 min = selection.getMinimumPoint();
        BlockVector3 max = selection.getMaximumPoint();

        double dx = axisDistance(loc.getX(), min.x(), max.x());
        double dy = axisDistance(loc.getY(), min.y(), max.y());
        double dz = axisDistance(loc.getZ(), min.z(), max.z());
        double distance = Math.sqrt(dx * dx + dy * dy + dz * dz);

        Verdict v = distance > THRESHOLD_BLOCKS ? Verdict.TOO_FAR : Verdict.OK;
        return new Result(v, distance, selectionWorld, playerWorld);
    }

    private static final Component CONFIRM_PROMPT = Component.text(" Did you mean to use this selection?");

    public static Component describe(Result r) {
        Component body =
                switch (r.verdict()) {
                    case DIFFERENT_WORLD ->
                        Component.text("Your WE selection is in ")
                                .append(highlight("'" + r.selectionWorld() + "'"))
                                .append(Component.text(" but you are in "))
                                .append(highlight("'" + r.playerWorld() + "'"))
                                .append(Component.text("."));
                    case TOO_FAR ->
                        Component.text("Your WE selection is ")
                                .append(highlight(Math.round(r.distance()) + " blocks"))
                                .append(Component.text(" away (threshold "))
                                .append(highlight(String.valueOf(THRESHOLD_BLOCKS)))
                                .append(Component.text(")."));
                    case OK -> null;
                };
        if (body == null) return Component.empty();
        return body.append(CONFIRM_PROMPT).color(NamedTextColor.RED);
    }

    private static Component highlight(String s) {
        return Component.text(s, NamedTextColor.WHITE);
    }

    private static double axisDistance(double p, int min, int max) {
        if (p < min) return min - p;
        if (p > max) return p - max;
        return 0;
    }
}
