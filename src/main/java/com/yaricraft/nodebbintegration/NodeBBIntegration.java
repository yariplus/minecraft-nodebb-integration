package com.yaricraft.nodebbintegration;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class NodeBBIntegration extends JavaPlugin {

    @Override
    public void onEnable() {
        SocketIOClient.create(this).runTaskLaterAsynchronously(this, 60);
        new TaskTick(this);

        this.saveDefaultConfig();

        setupChat();
        setupPermissions();
        if (!setupEconomy()) System.out.println("Vault economy not found.");

        getServer().getPluginManager().registerEvents(new NodeBBIntegrationListener(this), this);
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("register")) {
            return CommandRegister.execute(sender, cmd, label, args);
        }else if (cmd.getName().equalsIgnoreCase("nodebb")) {
            if (args.length == 0) {
                this.reloadConfig();
                sender.sendMessage("Reloaded Config.");
                System.out.println("Reloaded Config.");
            }else if (args.length == 1) {
                this.getConfig().set("APIKEY", args[0]);
                this.saveConfig();
                sender.sendMessage("Set new API key.");
                System.out.println("Set new API key.");
            }
            return true;
        }else{
            return false;
        }
    }

    public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;

    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
