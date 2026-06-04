package com.aliiensmp.aliienChatColorLite.cache;

import com.aliiensmp.core.lib.boostedyaml.YamlDocument;
import com.aliiensmp.core.lib.boostedyaml.block.implementation.Section;
import org.bukkit.command.CommandSender;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Loads and exposes configured chat colors.
 */
public class ChatColorRegistry {

    private volatile Map<String, ChatColorCache> colors = Map.of();

    /**
     * Rebuilds the color cache from config.
     *
     * @param config loaded plugin config
     * @return number of colors loaded
     */
    public int reload(YamlDocument config) {
        Map<String, ChatColorCache> loadedColors = new LinkedHashMap<>();
        Section colorsSection = config.getSection("colors");

        if (colorsSection != null) {
            for (String key : colorsSection.getRoutesAsStrings(false)) {
                String id = normalize(key);
                String format = colorsSection.getString(key + ".color", "");
                String permission = colorsSection.getString(key + ".permission", "");
                loadedColors.put(id, new ChatColorCache(id, format, permission));
            }
        }

        colors = Map.copyOf(loadedColors);
        return colors.size();
    }

    /**
     * Gets a cached color by id.
     *
     * @param colorId requested color id
     * @return cached color, or {@code null} when unknown
     */
    public ChatColorCache getColor(String colorId) {
        String normalizedColor = normalizeColorId(colorId);
        return normalizedColor == null ? null : colors.get(normalizedColor);
    }

    /**
     * Gets the MiniMessage format for a configured color.
     *
     * @param colorId requested color id
     * @return configured color format, or an empty string when unknown
     */
    public String getFormat(String colorId) {
        ChatColorCache color = getColor(colorId);
        return color == null ? "" : color.format();
    }

    /**
     * Checks whether a sender can use a color.
     *
     * @param sender sender to check
     * @param color cached color
     * @return {@code true} when the color has no permission or the sender has it
     */
    public boolean hasAccess(CommandSender sender, ChatColorCache color) {
        String permission = color.permission();
        return permission == null || permission.isBlank() || sender.hasPermission(permission);
    }

    /**
     * Gets all cached colors.
     *
     * @return immutable color map keyed by normalized id
     */
    public Map<String, ChatColorCache> getCachedColors() {
        return colors;
    }

    /**
     * Normalizes a color id and verifies it exists.
     *
     * @param colorId raw color id
     * @return normalized id, or {@code null} when the id is blank or unknown
     */
    public String normalizeColorId(String colorId) {
        String normalizedColor = normalize(colorId);
        return normalizedColor == null || !colors.containsKey(normalizedColor) ? null : normalizedColor;
    }

    /**
     * Normalizes a color id for cache lookup.
     *
     * @param colorId raw color id
     * @return normalized id, or {@code null} when blank
     */
    private String normalize(String colorId) {
        if (colorId == null || colorId.isBlank()) {
            return null;
        }
        return colorId.toLowerCase(Locale.ROOT);
    }
}
