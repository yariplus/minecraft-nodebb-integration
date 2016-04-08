# NodeBB Integration
This Bukkit plugin is a companion app for [Minecraft Integration](https://community.nodebb.org/topic/3559/nodebb-plugin-minecraft-integration-minecraft-integration), a [NodeBB](https://nodebb.org/) forum plugin. It establishes a WebSocket connection to the forum which allows communication between the forum and your Minecraft server. This allows information on your Minecraft server and players to be displayed on your forum via widgets, user profiles, posts, shoutboxes, etc.. Your forum can also send events back to the server such as posts, messages, awards, groups. The plugin is a WebSocket client only and exposes it's API only to the Minecraft Integration plugin. The WebSocket client is run asynchronously to prevent network lag from interfering with normal server processes. If your forum uses a secure connection, this plugin's connection will be secure as well.

## Requirements

Requires Java 8 or higher.

### What is NodeBB?

Form the [NodeBB](https://nodebb.org/) Docs:

![NodeBB logo](https://i.imgur.com/3yj1n6N.png)

**NodeBB** is a next-generation discussion platform that utilizes web sockets for instant interactions and real-time notifications. NodeBB forums have many modern features out of the box such as social network integration and streaming discussions.

## What does the NodeBB Integration plugin do?

NodeBB Integration sends socket events to your forum when a Bukkit event occurs. The forum listens for these events and displays relevant information to forum users.

- Display server info such as online time, plugins, mods, and online players, updated in real-time.
- Show rankings such as most money, playtime, or McMMO skills on your forum, also updated in real-time.
- Synchronize server chat and forum shoutboxes.
- Synchronize server ranks with forum group membership.
- Apply ranks to Minecraft players based on Forum usage. (Posts, reputation, etc..)

## Plugin Configuration

After placing the NodeBB Integration jar into your plugins folder and restarting your Minecraft server, a config.yml will be created in the folder `plugins/NodeBB_Integration` with the following options:

Option | Usage
---------------|-------------
**FORUMURL** | The web address of the forum the plugin is connecting to. e.g., https://community.example.com/. Can use color codes with '&'.
**FORUMNAME** | The name of your forum, displayed on messages sent by this plugin. Can use color codes with '&'.
**APIKEY** | The API Key for your forum from the server settings section of Minecraft Integration.
**SOCKETNAMESPACE** | Advanced use only. Default is `plugins`
**PLUGINID** | Advanced use only. Default is `MinecraftIntegration`

The FORUMURL and APIKEY are required. You can save them to the config.yml or using the commands below. After editing the configuration, use the command `/nodebb reload` to reconnect to the forum. *No server restart is required.*

## Commands

### For OPs

These commands are available to all OPs and any players that have the permission **nodebb.admin**

**/nodebb**
Displays all plugin commands.

!["Picture of commands using /nodebb"](http://puu.sh/lYDZZ/14c755d95a.png)

**/nodebb reload**
Reloads the config.yml from disk and reconnects to the forum.

**/nodebb key [key]**
Gets or sets the forum API Key and reloads.

**/nodebb url [url]**
Gets or sets the forum URL and reloads.

**/nodebb name [name]**
Gets or sets the forum name and reloads.

**/nodebb debug [toggle]**
Displays information useful for fixing errors. Use the toggle option to turn on verbose logging.

### For Players

All players have these commands. No permission is required.

**/register [EMAIL] [PASSWORD]**
Connects the forum account associated with the email entered to the players UUID, allowing additional features on your forum.

## Setting up your NodeBB forum

NodeBB Integration is designed for use with the NodeBB plugin [Minecraft Integration](https://community.nodebb.org/topic/3559/nodebb-plugin-minecraft-integration-minecraft-integration).

Minecraft Integration provides a GUI to manage:

- Forum accounts linked to a player's UUID.
- Linking a users' avatar to their Minecraft skin.

as well as adding Widgets to your forum:

- Server Status (dynamically updated)
- TPS Graph
- Player Rankings (for Playtime, Money, or McMMO stats.)
- Player Graphs (for displaying online players or player rankings over time.)

### Example Widgets

#### Server Status

Dynamically updates player count, avatars, online status, and plugins.

![](http://yariplus.x10.mx/images/widgetServerStatus.png)

#### Player Graph

Updates dynamically.

![](http://yariplus.x10.mx/images/widgetOnlinePlayersGraph.png)

#### Player Rankings

Dynamically updates the statistic and player order.

![](http://yariplus.x10.mx/images/widgetTopPlayersList.png)

## Advanced Configuration

Advanced users can create their own NodeBB plugin to listen for Bukkit events using the socket.io module.

The SOCKETNAMESPACE and PLUGINID must be changed to their appropriate values in the config.yml.

> var socketPlugins = require('./socket.io/plugins');
>
> socketPlugins.MyPlugin = {
>   eventPlayerJoin: function (socket, data, callback) {
>     // The event is parsed into a plain js object.
>     console.log(data.player.name);
>   }
> }

## Project Information

**License:**
CC0 or MIT License.

**Source:**
[https://github.com/yariplus/bukkit-plugin-nodebb-integration](https://github.com/yariplus/bukkit-plugin-nodebb-integration)
