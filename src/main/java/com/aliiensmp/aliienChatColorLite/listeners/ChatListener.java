package com.aliiensmp.aliienChatColorLite.listeners;

import com.aliiensmp.aliienChatColorLite.service.PlayerColorService;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * Applies selected chat colors to player messages.
 */
public class ChatListener implements Listener {

    private final PlayerColorService playerColorService;

    /**
     * Creates the chat listener.
     *
     * @param playerColorService player color service
     */
    public ChatListener(PlayerColorService playerColorService) {
        this.playerColorService = playerColorService;
    }

    /**
     * Colors async chat messages using the preloaded player cache.
     *
     * @param event async chat event
     */
    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        event.message(playerColorService.applyColor(event.getPlayer(), event.message()));
    }
}
