package com.aliiensmp.aliienChatColorLite.service;

import com.aliiensmp.aliienChatColorLite.AliienChatColorLite;
import com.aliiensmp.aliienChatColorLite.cache.ChatColorRegistry;
import com.aliiensmp.aliienChatColorLite.cache.PlayerColorCache;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

/**
 * Handles player color persistence and chat formatting.
 */
public class PlayerColorService {

    private final AliienChatColorLite plugin;
    private final ChatColorRegistry colorRegistry;
    private final PlayerColorCache playerColorCache;
    private final NamespacedKey colorKey;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    /**
     * Creates the player color service.
     *
     * @param plugin owning plugin instance
     * @param colorRegistry configured color registry
     * @param playerColorCache runtime player color cache
     */
    public PlayerColorService(
            AliienChatColorLite plugin,
            ChatColorRegistry colorRegistry,
            PlayerColorCache playerColorCache
    ) {
        this.plugin = plugin;
        this.colorRegistry = colorRegistry;
        this.playerColorCache = playerColorCache;
        this.colorKey = new NamespacedKey(plugin, "chosen_chat_color");
    }

    /**
     * Saves and caches a player's active chat color.
     *
     * @param player player to update
     * @param colorName normalized color id
     */
    public void setPlayerColor(Player player, String colorName) {
        player.getPersistentDataContainer().set(colorKey, PersistentDataType.STRING, colorName);
        playerColorCache.put(player.getUniqueId(), colorName);
    }

    /**
     * Clears a player's active chat color.
     *
     * @param player player to update
     */
    public void removePlayerColor(Player player) {
        player.getPersistentDataContainer().remove(colorKey);
        playerColorCache.remove(player.getUniqueId());
    }

    /**
     * Loads a player's persisted chat color into memory.
     *
     * @param player player whose data should be cached
     */
    public void loadPlayerColor(Player player) {
        String colorId = player.getPersistentDataContainer().get(colorKey, PersistentDataType.STRING);
        String normalizedColor = colorRegistry.normalizeColorId(colorId);

        if (normalizedColor == null) {
            playerColorCache.remove(player.getUniqueId());
            return;
        }

        playerColorCache.put(player.getUniqueId(), normalizedColor);
    }

    /**
     * Removes a player from the runtime cache.
     *
     * @param player player leaving the server
     */
    public void unloadPlayerColor(Player player) {
        playerColorCache.remove(player.getUniqueId());
    }

    /**
     * Refreshes cached choices for every online player on their owning region.
     */
    public void refreshOnlinePlayers() {
        playerColorCache.clear();
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.getScheduler().run(plugin, scheduledTask -> loadPlayerColor(player), null);
        }
    }

    /**
     * Gets a player's cached active color.
     *
     * @param player player to inspect
     * @return color id, or {@code null} when no color is active
     */
    public String getPlayerColor(Player player) {
        return playerColorCache.get(player.getUniqueId());
    }

    /**
     * Applies the player's selected color to a chat message.
     *
     * @param player chat sender
     * @param message original chat message
     * @return colored message, or the original message when no color is selected
     */
    public Component applyColor(Player player, Component message) {
        String colorId = getPlayerColor(player);
        if (colorId == null) {
            return message;
        }

        String format = colorRegistry.getFormat(colorId);
        if (format.isBlank()) {
            return message;
        }

        return miniMessage.deserialize(format + "<content>", Placeholder.component("content", message));
    }
}
