package com.radiofreederp.nodebbintegration.commands;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationSponge;
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
    private NodeBBIntegrationSponge plugin;
    public CommandNodeBBSponge(NodeBBIntegrationSponge plugin) {
        this.plugin = plugin;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player || src instanceof ConsoleSource)) return CommandResult.empty();

        if (args.getOne("value").isPresent()) {

            // Set a config value.
            String value = args.getOne("value").get().toString();
            switch (args.getOne("command").get().toString().toLowerCase()) {
                case "name":
                    plugin.getPluginConfig().setForumName(value);
                    src.sendMessage(Text.builder("Set name to " + value).color(TextColors.AQUA).build());
                    break;
                case "url":
                    plugin.getPluginConfig().setForumURL(value);
                    src.sendMessage(Text.builder("Set url to " + value).color(TextColors.AQUA).build());
                    break;
                case "key":
                    plugin.getPluginConfig().setForumAPIKey(value);
                    src.sendMessage(Text.builder("Set key to " + value).color(TextColors.AQUA).build());
                    break;
                case "live":
                    plugin.getPluginConfig().setSocketAddress(value);
                    src.sendMessage(Text.builder("Set live to " + value).color(TextColors.AQUA).build());
                    break;
                case "debug":
                    src.sendMessage(Text.builder("set debug").color(TextColors.AQUA).build());
                    break;
                default:
                case "help":
                    src.sendMessage(Text.builder("display help").color(TextColors.AQUA).build());
                    break;
            }

            // Save config.
            plugin.getPluginConfig().save();

        } else {

            // Get a value.
            switch (args.getOne("command").get().toString().toLowerCase()) {
                case "name":
                    src.sendMessage(Text.builder(plugin.getPluginConfig().getForumName()).color(TextColors.AQUA).build());
                    break;
                case "url":
                    src.sendMessage(Text.builder(plugin.getPluginConfig().getForumURL()).color(TextColors.AQUA).build());
                    break;
                case "live":
                    src.sendMessage(Text.builder(plugin.getPluginConfig().getSocketAddress()).color(TextColors.AQUA).build());
                    break;
                case "key":
                    src.sendMessage(Text.builder(plugin.getPluginConfig().getForumAPIKey()).color(TextColors.AQUA).build());
                    break;
                case "debug":
                    src.sendMessage(Text.builder("TODO get debug").color(TextColors.AQUA).build());
                    break;
                case "reload":
                    src.sendMessage(Text.builder("TODO reloading").color(TextColors.AQUA).build());
                    break;
                default:
                case "help":
                    src.sendMessage(Text.builder("TODO display help").color(TextColors.AQUA).build());
                    break;
            }
        }

        return CommandResult.success();
    }
}
