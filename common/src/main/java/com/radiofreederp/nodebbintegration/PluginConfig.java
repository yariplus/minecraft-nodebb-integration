package com.radiofreederp.nodebbintegration;

/**
 * Created by Yari on 3/6/2016.
 */
public interface PluginConfig {
    void reload();
    void save();
    void updateMessages();

    String getForumURL();
    String getForumName();
    String getForumAPIKey();
    String getSocketAddress();
    String[] getSocketTransports();
    String getSocketNamespace();

    void setForumURL(String url);
    void setForumName(String name);
    void setSocketAddress(String address);
    void addSocketTransport(String transport);
    void removeSocketTransport(String transport);
    void setForumAPIKey(String key);

    String[] getMessage(String key);

    void addMessageLine(String key, String line);
    void removeMessageLine(String key);

    enum ConfigOptions {
        FORUMURL         ("forum.url"),
        FORUMNAME        ("forum.name"),
        FORUMAPIKEY      ("forum.apikey"),
        SOCKETADDRESS    ("socketio.address"),
        SOCKETTRANSPORTS ("socketio.transports"),
        SOCKETNAMESPACE  ("socketio.namespace"),

        MSGS          ("messages."),
        MSGS_REGISTER (MSGS.getKey() + "register."),
        MSGS_NBB      (MSGS.getKey() + "nodebb."),

        MSG_REGISTER  (MSGS_REGISTER.getKey() + "RegSuccess"),
        MSG_CREATE    (MSGS_REGISTER.getKey() + "CreatedNewAccount"),
        MSG_FAILKEY   (MSGS_REGISTER.getKey() + "FailKey"),
        MSG_FAILDB    (MSGS_REGISTER.getKey() + "FailDB"),
        MSG_BADRES    (MSGS_REGISTER.getKey() + "BadRes"),
        MSG_FAILDATA  (MSGS_REGISTER.getKey() + "FailData"),
        MSG_ERROR     (MSGS_REGISTER.getKey() + "ParsingError"),

        MSG_FORUMURL_GET      (MSGS_NBB.getKey() + FORUMURL.getKey() + ".get"),
        MSG_FORUMURL_SET      (MSGS_NBB.getKey() + FORUMURL.getKey() + ".set"),
        MSG_FORUMNAME_GET     (MSGS_NBB.getKey() + FORUMNAME.getKey() + ".get"),
        MSG_FORUMNAME_SET     (MSGS_NBB.getKey() + FORUMNAME.getKey() + ".set"),
        MSG_FORUMAPIKEY_GET   (MSGS_NBB.getKey() + FORUMAPIKEY.getKey() + ".get"),
        MSG_FORUMAPIKEY_SET   (MSGS_NBB.getKey() + FORUMAPIKEY.getKey() + ".set"),
        MSG_SOCKETADDRESS_GET (MSGS_NBB.getKey() + SOCKETADDRESS.getKey() + ".get"),
        MSG_SOCKETADDRESS_SET (MSGS_NBB.getKey() + SOCKETADDRESS.getKey() + ".set");

        private String key;

        public String getKey() {
            return key;
        }

        ConfigOptions(String key) {
            this.key = key;
        }
    }
}
