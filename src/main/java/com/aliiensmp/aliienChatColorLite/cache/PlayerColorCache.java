package com.aliiensmp.aliienChatColorLite.cache;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerColorCache {

    private final Map<UUID, String> colorsByPlayer = new ConcurrentHashMap<>();

    /**
     * Stores a player's active color in memory.
     *
     * @param playerId player unique id
     * @param colorId normalized color id
     */
    public void put(UUID playerId, String colorId) {
        colorsByPlayer.put(playerId, colorId);
    }

    /**
     * Reads the cached color for a player.
     *
     * @param playerId player unique id
     * @return color id, or {@code null} when no color is cached
     */
    public String get(UUID playerId) {
        return colorsByPlayer.get(playerId);
    }

    /**
     * Removes a player from the cache.
     *
     * @param playerId player unique id
     */
    public void remove(UUID playerId) {
        colorsByPlayer.remove(playerId);
    }

    /**
     * Clears all cached player choices.
     */
    public void clear() {
        colorsByPlayer.clear();
    }
}
