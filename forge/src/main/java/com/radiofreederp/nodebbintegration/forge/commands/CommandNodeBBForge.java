package com.radiofreederp.nodebbintegration.forge.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationForge;
import com.radiofreederp.nodebbintegration.commands.CommandNodeBB;
import com.radiofreederp.nodebbintegration.utils.NBBPlugin;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.World;

public class CommandNodeBBForge extends CommandBase {
	private CommandNodeBB command;

	public CommandNodeBBForge(NodeBBIntegrationForge plugin) {
		this.command = new CommandNodeBB();
	}

	@Override
	public String getCommandName() {
		return "nodebb";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "nodebb <command>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		World world = sender.getEntityWorld();

		if (world.isRemote) return;

		String action;
		String option = null;
		String value = null;

		switch (args.length) {
			default:
			case 0:
				action = "help";
				break;
			case 1:
				action = "get";
				option = args[0];
				break;
			case 2:
				action = "set";
				option = args[0];
				value = args[1].replace('_', ' ');
				break;
		}

		command.doCommand(sender, action, option, value);
	}
}
