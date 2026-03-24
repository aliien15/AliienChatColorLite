package com.aliiensmp.aliienChatColorLite.listeners;

import com.aliiensmp.aliienChatColorLite.ChatColorManager;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {

    private final ChatColorManager colorManager;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ChatListener(ChatColorManager colorManager) {
        this.colorManager = colorManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        Player player = event.getPlayer();
        String colorId = colorManager.getPlayerColor(player);

        if (colorId != null) {
            String colorFormat = colorManager.getFormat(colorId);
            Component currentMessage = event.message();
            Component coloredMessage = mm.deserialize(colorFormat + "<content>", Placeholder.component("content", currentMessage));
            event.message(coloredMessage);
        }
    }
}
