package com.radiofreederp.nodebbintegration;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Yari on 3/6/2016.
 */
public abstract class PluginConfig {

    protected NodeBBIntegrationPlugin plugin;
    protected MinecraftServer server;

    abstract public String getString(ConfigOption option);
    abstract public List<String> getArray(ConfigOption option);
    abstract public void setString(ConfigOption option, String value);
    abstract public void setArray(ConfigOption option, List<String> value);

    abstract public void reload();
    abstract public void save();
    abstract public void updateMessages();

    abstract public Object getPlayerData();
    abstract public JSONObject getPlayerVotes(JSONObject req);

    // Get

    public String getForumURL() {
        return getString(ConfigOption.FORUMURL);
    }

    public String getForumName() {
        return getString(ConfigOption.FORUMNAME);
    }

    public String getForumAPIKey() {
        return getString(ConfigOption.FORUMAPIKEY);
    }

    // TODO: Better defaults setup.
    public String getSocketAddress() {
        String address = getString(ConfigOption.SOCKETADDRESS);
        if (address.equals("https://live.example.com/")) address = getString(ConfigOption.FORUMURL);
        return address;
    }

    public List<String> getSocketTransports() {
        return getArray(ConfigOption.SOCKETTRANSPORTS);
    }

    public String getSocketNamespace() {
        // Appended dot here is intentional.
        return getString(ConfigOption.SOCKETNAMESPACE) + ".";
    }

    // Set

    public void setForumURL(String url) {
        setString(ConfigOption.FORUMURL, url);
    }

    public void setForumName(String name) {
        setString(ConfigOption.FORUMNAME, name);
    }

    public void setForumAPIKey(String key) {
        setString(ConfigOption.FORUMAPIKEY, key);
    }

    public void setSocketAddress(String address) {
        setString(ConfigOption.SOCKETADDRESS, address);
    }

    public void addSocketTransport(String transport) {
        List<String> transports = getArray(ConfigOption.SOCKETTRANSPORTS);
        transports.add(transport);
        setArray(ConfigOption.SOCKETTRANSPORTS, transports);
    }

    public void removeSocketTransport(String transport) {
        List<String> transports = getArray(ConfigOption.SOCKETTRANSPORTS);
        transports.remove(transport);
        setArray(ConfigOption.SOCKETTRANSPORTS, transports);
    }

    public enum ConfigOption {
        FORUMURL         ("forum.url"),
        FORUMNAME        ("forum.name"),
        FORUMAPIKEY      ("forum.apikey"),
        SOCKETADDRESS    ("socketio.address"),
        SOCKETTRANSPORTS ("socketio.transports"),
        SOCKETNAMESPACE  ("socketio.namespace"),

        MSGS          ("messages."),
        MSGS_REGISTER (MSGS.getKey() + "register."),
        MSGS_NBB      (MSGS.getKey() + "nodebb."),

        MSG_REG_ALERT        (MSGS_REGISTER.getKey() + "Alert"),
        MSG_REG_ASSERTPARAMS (MSGS_REGISTER.getKey() + "AssertParameters"),
        MSG_REG_DISCONNECTED (MSGS_REGISTER.getKey() + "Disconnected"),

        MSG_REG_REGISTER  (MSGS_REGISTER.getKey() + "RegSuccess"),
        MSG_REG_CREATE    (MSGS_REGISTER.getKey() + "CreatedNewAccount"),
        MSG_REG_FAILKEY   (MSGS_REGISTER.getKey() + "FailKey"),
        MSG_REG_FAILDB    (MSGS_REGISTER.getKey() + "FailDB"),
        MSG_REG_BADRES    (MSGS_REGISTER.getKey() + "BadRes"),
        MSG_REG_FAILDATA  (MSGS_REGISTER.getKey() + "FailData"),
        MSG_REG_ERROR     (MSGS_REGISTER.getKey() + "ParsingError"),

        MSG_HELP (MSGS_NBB.getKey() + "help"),

        MSG_FORUMURL_GET      (MSGS_NBB.getKey() + ConfigOption.FORUMURL.getKey() + ".get"),
        MSG_FORUMURL_SET      (MSGS_NBB.getKey() + ConfigOption.FORUMURL.getKey() + ".set"),
        MSG_FORUMNAME_GET     (MSGS_NBB.getKey() + ConfigOption.FORUMNAME.getKey() + ".get"),
        MSG_FORUMNAME_SET     (MSGS_NBB.getKey() + ConfigOption.FORUMNAME.getKey() + ".set"),
        MSG_FORUMAPIKEY_GET   (MSGS_NBB.getKey() + ConfigOption.FORUMAPIKEY.getKey() + ".get"),
        MSG_FORUMAPIKEY_SET   (MSGS_NBB.getKey() + ConfigOption.FORUMAPIKEY.getKey() + ".set"),
        MSG_SOCKETADDRESS_GET (MSGS_NBB.getKey() + ConfigOption.SOCKETADDRESS.getKey() + ".get"),
        MSG_SOCKETADDRESS_SET (MSGS_NBB.getKey() + ConfigOption.SOCKETADDRESS.getKey() + ".set");

        private String key;

        public String getKey() {
            return key;
        }

        ConfigOption(String key) {
            this.key = key;
        }
    }
}
