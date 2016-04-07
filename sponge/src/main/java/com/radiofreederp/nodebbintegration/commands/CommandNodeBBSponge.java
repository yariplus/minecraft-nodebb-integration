package com.radiofreederp.nodebbintegration.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Yari on 4/5/2016.
 */
public class CommandNodeBBSponge implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        switch (args.getOne("command").orElse(Text.of("help")).toString()) {
            default:
            case "help": return help(src, args);
        }
    }

    private CommandResult help(CommandSource src, CommandContext args) {
        if (src instanceof Player) {
            Player player = (Player)src;
            player.sendMessage(Text.builder("Works").color(TextColors.AQUA).build());
        } else if (src instanceof ConsoleSource) {
            ConsoleSource console = (ConsoleSource) src;
            console.sendMessage(Text.builder("Works").color(TextColors.AQUA).build());
        }
        return CommandResult.success();
    }
}
