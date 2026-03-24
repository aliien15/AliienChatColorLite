package com.aliiensmp.aliienChatColorLite.hooks;

import com.aliiensmp.aliienChatColorLite.AliienChatColorLite;
import com.aliiensmp.aliienChatColorLite.ChatColorManager;
import com.aliiensmp.aliienChatColorLite.utils.ChatColorCache;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private final AliienChatColorLite plugin;
    private final ChatColorManager colorManager;

    public PlaceholderAPIExpansion(AliienChatColorLite plugin, ChatColorManager colorManager) {
        this.plugin = plugin;
        this.colorManager = colorManager;
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

        String colorId = colorManager.getPlayerColor(player);

        // %aliienchatcolor_active%
        if (params.equalsIgnoreCase("active"))
            return colorId == null ?  "None" : colorId;

        // %aliienchatcolor_format%
        if (params.equalsIgnoreCase("format"))
            return colorId == null ? "None" : colorManager.getFormat(colorId);

        // %aliienchatcolor_has_(color)%
        if (params.startsWith("has_")) {
            String requestedColor = params.substring(4).toLowerCase();

            ChatColorCache color = colorManager.getCachedColors().get(requestedColor);
            if (color == null)
                return "False";

            return player.hasPermission(color.getPermission()) ? "True" : "False";
        }

        return null;
    }
}
