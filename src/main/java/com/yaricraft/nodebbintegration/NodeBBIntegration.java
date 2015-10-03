package com.yaricraft.nodebbintegration;

import com.github.nkzawa.socketio.client.Ack;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.JSONException;
import org.json.JSONObject;

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
            // If we're not connected, don't do anything.
            if (SocketIOClient.getSocket() == null) return false;

            if (args.length != 2) {
                sender.sendMessage("Please use \"/register [email] [password]\"");
                return false;
            }

            try {
                sender.sendMessage("Registering you on " + this.getConfig().getString("FORUMNAME") + "...");
            }catch (Exception e) {
                e.printStackTrace();
            }

            JSONObject obj = new JSONObject();

            try {
                obj.put("email", args[0]);
                obj.put("password", args[1]);
                obj.put("name", sender.getName());
                obj.put("id", ((Player) sender).getUniqueId().toString());
                obj.put("key", this.getConfig().getString("APIKEY"));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("Sending " + SocketIOClient.getNamespace() + "commandRegister");

            final CommandSender commandSender = sender;
            final String forum = this.getConfig().getString("FORUMNAME");

            SocketIOClient.getSocket().emit(SocketIOClient.getNamespace() + "commandRegister", obj, new Ack() {
                @Override
                public void call(Object... args) {
                    if (args[0] == null) {
                        try {
                            JSONObject result = ((JSONObject) args[1]);
                            System.out.println("commandRegister success: " + result.getString("task"));

                            String message = "";
                            switch (result.getString("task")) {
                                case "REGISTER":
                                    message = "Success! Registered your account on " + forum;
                                    break;
                                case "REREGISTER":
                                    message = "Success! Updated your profile on " + forum;
                                    break;
                            }

                            commandSender.sendMessage(message);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("commandRegister error: Failed to parse result.");
                            commandSender.sendMessage("Registration error: Internal parsing error. Please inform an administrator.");
                        }
                    }else{
                        try {
                            JSONObject err = ((JSONObject) args[0]);
                            System.out.println("commandRegister error: " + err.getString("message"));

                            String error = "";
                            switch (err.getString("message")) {
                                case "FAILPASS":
                                    error = "Invalid Password";
                                    break;
                                default:
                                    error = err.getString("message");
                                    break;
                            }

                            commandSender.sendMessage("Registration error: " + error);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            System.out.println("commandRegister error: Failed to parse error.");
                            commandSender.sendMessage("Registration error: Internal parsing error. Please inform an administrator.");
                        }
                    }
                }
            });
            return true;
        }else if (cmd.getName().equalsIgnoreCase("nodebb")) {
            if (args.length == 0) {
                this.reloadConfig();
                sender.sendMessage("Reloaded Config.");
                System.out.println("Reloaded Config.");
            } else if (args.length == 1) {
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
