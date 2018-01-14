package com.radiofreederp.nodebbintegration.sponge.commands;

import com.radiofreederp.nodebbintegration.commands.CommandNodeBB;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import java.util.logging.Level;

public class CommandNodeBBSponge extends CommandNodeBB implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        // Make sure source can receive messages back.
        if (!(src instanceof Player || src instanceof ConsoleSource)) return CommandResult.empty();

        String action = "";
        String option = args.getOne("option").orElse("help").toString().toLowerCase();
        String value = "";

        if (args.getOne("value").isPresent()) {
            action = "set";
            value = args.getOne("value").orElse("help").toString();

            // Add remaining text to value.
            if (args.getOne("remaining").isPresent()) {
                value += " " + args.getOne("remaining").toString(); // TODO: This doesn't actually work.
            }
        }else{
            action = "get";
        }

        return doCommand(src, action, option, value) ? CommandResult.success() : CommandResult.empty();
    }
}
