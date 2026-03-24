package com.aliiensmp.aliienChatColorLite;

import com.aliiensmp.aliienChatColorLite.utils.ChatColorCache;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.block.implementation.Section;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.Map;

public class ChatColorManager {

    private final AliienChatColorLite plugin;
    private final YamlDocument config;
    private final YamlDocument messages;
    private final NamespacedKey colorKey;

    private final Map<String, ChatColorCache> colorCache = new HashMap<>();

    private String invalidColorMsg;
    private String successMsg;
    private String incorrectCmdUsageMsg;
    private String noPermsMsg;
    private String reloadMsg;
    private String failReloadMsg;
    private String clearColorMsg;
    private String newVersionMsg;

    public ChatColorManager(AliienChatColorLite plugin, YamlDocument config, YamlDocument messages) {
        this.plugin = plugin;
        this.config = config;
        this.messages = messages;
        this.colorKey = new NamespacedKey(plugin, "chosen_chat_color");
    }

    public void setPlayerColor(Player player, String colorName) {
        player.getPersistentDataContainer().set(colorKey, PersistentDataType.STRING, colorName);
    }

    public void removePlayerColor(Player player) {
        player.getPersistentDataContainer().remove(colorKey);
    }

    public String getPlayerColor(Player player) {
        if (player.getPersistentDataContainer().has(colorKey, PersistentDataType.STRING)) {
            return player.getPersistentDataContainer().get(colorKey, PersistentDataType.STRING);
        }
        return null;
    }

    public String getFormat(String colorId) {
        ChatColorCache info = colorCache.get(colorId.toLowerCase());
        return (info != null) ? info.getFormat() : "";
    }

    public void loadColorsToCache() {
        colorCache.clear();

        invalidColorMsg = messages.getString("messages.invalid-color", "<red>Sorry, that color is invalid/doesn't exist!");
        successMsg = messages.getString("messages.success", "<green>You have successfully updated your chat color!");
        incorrectCmdUsageMsg = messages.getString("messages.incorrect-cmd-usage", "<red>This command was not used correctly!");
        noPermsMsg = messages.getString("messages.no-perms", "<red>You do not have permission to use this!");
        reloadMsg = messages.getString("messages.reload", "<green>AliienChatColorLite has been reloaded!");
        failReloadMsg = messages.getString("messages.fail-reload", "<red>There was an error while reloading (check console)");
        clearColorMsg = messages.getString("messages.clear-color", "<green>You have successfully cleared the chat color!");
        newVersionMsg = messages.getString("messages.new-version", "<green>A new AliienChatColorLite version is now available!");

        Section colorsSection = config.getSection("colors");
        if (colorsSection != null) {
            for (String key : colorsSection.getRoutesAsStrings(false)) {
                String format = colorsSection.getString(key + ".color", "");
                String perm = colorsSection.getString(key + ".permission", "");
                colorCache.put(key, new ChatColorCache(key, format, perm));
            }
            plugin.getLogger().info("Successfully loaded " + colorCache.size() + " chat colors into cache!");
        }
    }

    public Map<String, ChatColorCache> getCachedColors() { return colorCache; }

    public String getInvalidColorMsg() { return invalidColorMsg; }
    public String getSuccessMsg() { return successMsg; }
    public String getIncorrectCmdUsageMsg() { return incorrectCmdUsageMsg; }
    public String getNoPermsMsg() { return noPermsMsg; }
    public String getReloadMsg() { return reloadMsg; }
    public String getFailReloadMsg() { return failReloadMsg; }
    public String getClearColorMsg() { return clearColorMsg; }
    public String getNewVersionMsg() { return newVersionMsg; }
}