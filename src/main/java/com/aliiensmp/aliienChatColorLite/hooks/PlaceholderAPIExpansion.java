package com.aliiensmp.aliienChatColorLite.hooks;

import com.aliiensmp.aliienChatColorLite.AliienChatColorLite;
import com.aliiensmp.aliienChatColorLite.cache.ChatColorCache;
import com.aliiensmp.aliienChatColorLite.cache.ChatColorRegistry;
import com.aliiensmp.aliienChatColorLite.service.PlayerColorService;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private final AliienChatColorLite plugin;
    private final ChatColorRegistry colorRegistry;
    private final PlayerColorService playerColorService;

    /**
     * Creates the PlaceholderAPI expansion.
     *
     * @param plugin owning plugin instance
     * @param colorRegistry configured color registry
     * @param playerColorService player color service
     */
    public PlaceholderAPIExpansion(
            AliienChatColorLite plugin,
            ChatColorRegistry colorRegistry,
            PlayerColorService playerColorService
    ) {
        this.plugin = plugin;
        this.colorRegistry = colorRegistry;
        this.playerColorService = playerColorService;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "aliienchatcolor";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Aliien15";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getPluginMeta().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        if (offlinePlayer == null || !offlinePlayer.isOnline()) return "";
        Player player = offlinePlayer.getPlayer();
        if (player == null) return "";

        String colorId = playerColorService.getPlayerColor(player);

        // %aliienchatcolor_active%
        if (params.equalsIgnoreCase("active"))
            return colorId == null ?  "None" : colorId;

        // %aliienchatcolor_format%
        if (params.equalsIgnoreCase("format"))
            return colorId == null ? "None" : colorRegistry.getFormat(colorId);

        // %aliienchatcolor_has_(color)%
        if (params.startsWith("has_")) {
            String requestedColor = params.substring(4);

            ChatColorCache color = colorRegistry.getColor(requestedColor);
            if (color == null)
                return "False";

            return player.hasPermission(color.permission()) ? "True" : "False";
        }

        return null;
    }
}
