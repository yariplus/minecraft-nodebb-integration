package com.radiofreederp.nodebbintegration.forge.commands;

import com.radiofreederp.nodebbintegration.commands.CommandRegister;
import com.radiofreederp.nodebbintegration.utils.NBBPlugin;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandRegisterForge extends CommandBase {
	private CommandRegister command;

	public CommandRegisterForge() {
		this.command = new CommandRegister();
	}

	@Override
	public String getCommandName() {
		return "register";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		return "register <key>";
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		// Return if client world.
		if (sender.getEntityWorld().isRemote) return;
		if (!(sender instanceof EntityPlayerMP)) return;
		EntityPlayerMP player = (EntityPlayerMP)sender;

		String pkey, uuid = null, name = null;
		if (args.length > 0) {
			pkey = args[0];
			uuid = player.getGameProfile().getId().toString();
			name = player.getGameProfile().getName();
		} else {
			pkey = null;
		}

		command.doCommand(sender, pkey, uuid, name);
	}
}
