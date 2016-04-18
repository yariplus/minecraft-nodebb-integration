package com.radiofreederp.nodebbintegration.sponge.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationSponge;
import com.radiofreederp.nodebbintegration.commands.CommandNodeBB;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;

/**
 * Created by Yari on 4/5/2016.
 */
public class CommandNodeBBSponge implements CommandExecutor {

    private final CommandNodeBB command;

    public CommandNodeBBSponge(NodeBBIntegrationSponge plugin) {
        this.command = new CommandNodeBB(plugin);
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        // Make sure source can receive messages back.
        if (!(src instanceof Player || src instanceof ConsoleSource)) return CommandResult.empty();

        String action;
        String option = args.getOne("option").orElse("help").toString().toLowerCase();
        String value = null;

        if (args.getOne("value").isPresent()) {
            action = "set";
            value = args.getOne("value").get().toString();

            // Add remaining text to value.
            if (args.getOne("remaining").isPresent()) {
                value += " " + args.getOne("remaining").get().toString();
            }
        }else{
            action = "get";
        }

        boolean success = this.command.doCommand(src, action, option, value);

        return success ? CommandResult.success() : CommandResult.empty();
    }
}
