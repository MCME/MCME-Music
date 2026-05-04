package com.mcmiddleearth.mcmemusic.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;

public final class CommandUtil {

    private CommandUtil() {}

    public static int requirePlayer(CommandContext<CommandSourceStack> ctx, Consumer<Player> body) {
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
