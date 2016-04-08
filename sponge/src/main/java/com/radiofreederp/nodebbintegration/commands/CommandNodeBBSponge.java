package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationPlugin;
import com.radiofreederp.nodebbintegration.NodeBBIntegrationSponge;
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

import java.io.IOException;

/**
 * Created by Yari on 4/5/2016.
 */
public class CommandNodeBBSponge implements CommandExecutor {
    private NodeBBIntegrationSponge plugin;
    public CommandNodeBBSponge(NodeBBIntegrationSponge plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        switch (args.getOne("command").orElse(Text.of("help")).toString()) {
            default:
            case "help": return help(src, args);
        }
    }

    private CommandResult help(CommandSource src, CommandContext args) {
        if (!(src instanceof Player || src instanceof ConsoleSource)) return CommandResult.empty();

        if (args.getOne("value").isPresent()) {
            String value = args.getOne("value").get().toString();
            switch (args.getOne("command").get().toString().toLowerCase()) {
                case "name":
                    plugin.getConfig().getNode("FORUMNAME").setValue(value);
                    src.sendMessage(Text.builder("Set name to " + value).color(TextColors.AQUA).build());
                    break;
                case "url":
                    plugin.getConfig().getNode("FORUMURL").setValue(value);
                    src.sendMessage(Text.builder("Set url to " + value).color(TextColors.AQUA).build());
                    break;
                case "live":
                    plugin.getConfig().getNode("socketio", "address").setValue(value);
                    src.sendMessage(Text.builder("Set live to " + value).color(TextColors.AQUA).build());
                    break;
                case "key":
                    plugin.getConfig().getNode("APIKEY").setValue(value);
                    src.sendMessage(Text.builder("Set key to " + value).color(TextColors.AQUA).build());
                    break;
                case "debug":
                    src.sendMessage(Text.builder("set debug").color(TextColors.AQUA).build());
                    break;
                default:
                case "help":
                    src.sendMessage(Text.builder("display help").color(TextColors.AQUA).build());
                    break;
            }
            try {
                plugin.getConfigManager().save(plugin.getConfig());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            switch (args.getOne("command").get().toString().toLowerCase()) {
                case "name":
                    src.sendMessage(Text.builder(plugin.getConfig().getNode("FORUMNAME").getString()).color(TextColors.AQUA).build());
                    break;
                case "url":
                    src.sendMessage(Text.builder(plugin.getConfig().getNode("FORUMURL").getString()).color(TextColors.AQUA).build());
                    break;
                case "live":
                    src.sendMessage(Text.builder(plugin.getConfig().getNode("socketio", "address").getString()).color(TextColors.AQUA).build());
                    break;
                case "key":
                    src.sendMessage(Text.builder(plugin.getConfig().getNode("APIKEY").getString()).color(TextColors.AQUA).build());
                    break;
                case "debug":
                    src.sendMessage(Text.builder("get debug").color(TextColors.AQUA).build());
                    break;
                case "reload":
                    src.sendMessage(Text.builder("reloading").color(TextColors.AQUA).build());
                    break;
                default:
                case "help":
                    src.sendMessage(Text.builder("display help").color(TextColors.AQUA).build());
                    break;
            }
        }

        return CommandResult.success();
    }
}
