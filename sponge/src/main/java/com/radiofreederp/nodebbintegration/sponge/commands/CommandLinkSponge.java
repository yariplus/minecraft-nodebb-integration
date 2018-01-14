package com.radiofreederp.nodebbintegration.sponge.commands;

import com.radiofreederp.nodebbintegration.commands.CommandLink;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

public class CommandLinkSponge extends CommandLink implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        // Source must be player.
        if (!(src instanceof Player)) return CommandResult.empty();

        Player sender = (Player) src;

        return doCommand(sender, null, sender.getUniqueId().toString(), sender.getName()) ? CommandResult.success() : CommandResult.empty();
    }
}
