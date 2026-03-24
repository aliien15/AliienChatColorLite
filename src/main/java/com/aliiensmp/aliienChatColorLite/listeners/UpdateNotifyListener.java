package com.aliiensmp.aliienChatColorLite.listeners;

import com.aliiensmp.aliienChatColorLite.AliienChatColorLite;
import com.aliiensmp.aliienChatColorLite.ChatColorManager;
import com.aliiensmp.aliienChatColorLite.utils.UpdateChecker;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateNotifyListener implements Listener {
    private final AliienChatColorLite plugin;
    private final ChatColorManager colorManager;
    private final String gistUrl;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public UpdateNotifyListener(AliienChatColorLite plugin, ChatColorManager colorManager, String gistUrl) {
        this.plugin = plugin;
        this.colorManager = colorManager;
        this.gistUrl = gistUrl;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (player.hasPermission("aliien.chatcolor.version-notify")) {
            new UpdateChecker(plugin, gistUrl).getVersion(version -> {
                if (!plugin.getPluginMeta().getVersion().equals(version)) {
                    player.sendMessage(mm.deserialize(colorManager.getNewVersionMsg()));
                }
            });
        }
    }
}