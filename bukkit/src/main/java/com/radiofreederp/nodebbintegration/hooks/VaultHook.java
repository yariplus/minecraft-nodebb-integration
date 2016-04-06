package com.radiofreederp.nodebbintegration.hooks;

import com.radiofreederp.nodebbintegration.NodeBBIntegrationBukkit;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class VaultHook
{
    public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;

    public static void hook(NodeBBIntegrationBukkit plugin)
    {
        if (setupChat(plugin))        NodeBBIntegrationBukkit.log("Hooked into Vault Chat.");
        if (setupPermissions(plugin)) NodeBBIntegrationBukkit.log("Hooked into Vault Permissions.");
        if (setupEconomy(plugin))     NodeBBIntegrationBukkit.log("Hooked into Vault Economy.");
    }

    private static boolean setupPermissions(NodeBBIntegrationBukkit plugin)
    {
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private static boolean setupChat(NodeBBIntegrationBukkit plugin)
    {
        RegisteredServiceProvider<Chat> chatProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private static boolean setupEconomy(NodeBBIntegrationBukkit plugin)
    {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
