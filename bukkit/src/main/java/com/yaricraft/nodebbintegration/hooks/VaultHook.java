package com.yaricraft.nodebbintegration.hooks;

import com.yaricraft.nodebbintegration.NodeBBIntegration;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class VaultHook
{
    public static Permission permission = null;
    public static Economy economy = null;
    public static Chat chat = null;

    public static void hook(NodeBBIntegration plugin)
    {
        if (setupChat(plugin))        NodeBBIntegration.log("Hooked into Vault Chat.");
        if (setupPermissions(plugin)) NodeBBIntegration.log("Hooked into Vault Permissions.");
        if (setupEconomy(plugin))     NodeBBIntegration.log("Hooked into Vault Economy.");
    }

    private static boolean setupPermissions(NodeBBIntegration plugin)
    {
        RegisteredServiceProvider<Permission> permissionProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private static boolean setupChat(NodeBBIntegration plugin)
    {
        RegisteredServiceProvider<Chat> chatProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private static boolean setupEconomy(NodeBBIntegration plugin)
    {
        RegisteredServiceProvider<Economy> economyProvider = plugin.getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
}
