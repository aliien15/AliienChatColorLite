package com.aliiensmp.aliienChatColorLite.listeners;

import com.aliiensmp.aliienChatColorLite.service.PlayerColorService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Keeps player color cache state aligned with player connections.
 */
public class PlayerConnectionListener implements Listener {

    private final PlayerColorService playerColorService;

    /**
     * Creates the connection listener.
     *
     * @param playerColorService player color service
     */
    public PlayerConnectionListener(PlayerColorService playerColorService) {
        this.playerColorService = playerColorService;
    }

    /**
     * Loads the player's persisted color into cache when they join.
     *
     * @param event join event
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        playerColorService.loadPlayerColor(event.getPlayer());
    }

    /**
     * Removes the player from the runtime cache when they leave.
     *
     * @param event quit event
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        playerColorService.unloadPlayerColor(event.getPlayer());
    }
}
