package com.radiofreederp.nodebbintegration.sponge.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationSponge;
import com.radiofreederp.nodebbintegration.commands.CommandRegister;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

/**
 * Created by Yari on 4/5/2016.
 */
public class CommandRegisterSponge implements CommandExecutor {

    private final CommandRegister command;
    private final NodeBBIntegrationPlugin plugin;

    public CommandRegisterSponge(NodeBBIntegrationSponge plugin) {
        this.command = new CommandRegister();
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        // Source must be player.
        if (!(src instanceof Player)) return CommandResult.empty();

        String pkey = args.getOne("pkey").isPresent() ? args.getOne("pkey").get().toString() : null;

        return this.command.doCommand(src, pkey, ((Player)src).getUniqueId().toString(), src.getName()) ? CommandResult.success() : CommandResult.empty();
    }
}
