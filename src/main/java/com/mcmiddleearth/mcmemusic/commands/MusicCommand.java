package com.mcmiddleearth.mcmemusic.commands;

import com.mcmiddleearth.mcmemusic.Main;
import com.mcmiddleearth.mcmemusic.Permission;
import com.mcmiddleearth.mcmemusic.util.CreateRegion;
import com.mcmiddleearth.mcmemusic.util.LoadRegion;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.HashMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public class MusicCommand {

    private final CreateRegion createRegion;
    private final LoadRegion loadRegion;
    private final Main main;

    public static HashMap<Player, Integer> playerListening = new HashMap<>();

    public MusicCommand(CreateRegion createRegion, LoadRegion loadRegion, Main main) {
        this.createRegion = createRegion;
        this.loadRegion = loadRegion;
        this.main = main;
    }

    private boolean canListen(CommandSourceStack src) {
        return src.getSender().hasPermission(Permission.LISTEN.getNode());
    }

    private boolean canManage(CommandSourceStack src) {
        return src.getSender().hasPermission(Permission.MANAGE.getNode());
    }

    public LiteralCommandNode<CommandSourceStack> node() {
        return Commands.literal("music")
                .requires(this::canListen)
                .executes(ctx -> showInfo(ctx.getSource()))
                .then(Commands.literal("info").executes(ctx -> showInfo(ctx.getSource())))
                .then(Commands.literal("on").executes(ctx -> CommandUtil.requirePlayer(ctx, this::onOn)))
                .then(Commands.literal("off").executes(ctx -> CommandUtil.requirePlayer(ctx, this::onOff)))
                .then(Commands.literal("loop").executes(ctx -> CommandUtil.requirePlayer(ctx, this::onLoop)))
                .then(Commands.literal("reload")
                        .requires(this::canManage)
                        .executes(ctx -> CommandUtil.requirePlayer(ctx, this::onReload)))
                .then(new MusicPlay(main).node())
                .then(new MusicStop(main).node())
                .then(new MusicCreateRP(createRegion, loadRegion).node())
                .then(new MusicCreateWG().node())
                .build();
    }

    private void onOn(Player p) {
        Main.getInstance().getPlayerManager().undeafen(p);
        p.sendMessage(Component.text("MCME music enabled.", NamedTextColor.GREEN));
    }

    private void onOff(Player p) {
        Main.getInstance().getPlayerManager().deafen(p);
        p.sendMessage(Component.text("MCME music disabled.", NamedTextColor.RED));
    }

    private void onLoop(Player p) {
        var pm = Main.getInstance().getPlayerManager();
        if (pm.isLooped(p)) {
            pm.unloop(p);
            p.sendMessage(Component.text("MCME music will now not loop.", NamedTextColor.RED));
        } else {
            pm.loop(p);
            p.sendMessage(Component.text("MCME music will now loop.", NamedTextColor.GREEN));
        }
    }

    private void onReload(Player p) {
        try {
            loadRegion.loadRegions();
            main.reloadConfig();
            p.sendMessage(Component.text("Regions and Config have been reloaded", NamedTextColor.GREEN));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int showInfo(CommandSourceStack src) {
        if (src.getSender().hasPermission(Permission.MANAGE.getNode())) {
            src.getSender()
                    .sendMessage(Component.text("Command: /music on|off|play <song name>", NamedTextColor.RED)
                            .append(Component.text(" OR ", NamedTextColor.GRAY))
                            .append(Component.text("/music create-rp <name> <id> <weight>", NamedTextColor.RED))
                            .append(Component.text(" OR ", NamedTextColor.GRAY))
                            .append(Component.text("/music create-wg <name>", NamedTextColor.RED)));
        } else {
            src.getSender().sendMessage(Component.text("Command: /music on|off|play <song name>", NamedTextColor.RED));
        }
        return Command.SINGLE_SUCCESS;
    }
}
