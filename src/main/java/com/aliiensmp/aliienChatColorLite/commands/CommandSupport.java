package com.aliiensmp.aliienChatColorLite.commands;

import com.aliiensmp.aliienChatColorLite.ChatColorManager;
import com.aliiensmp.core.utils.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Shared command helper methods.
 */
abstract class CommandSupport {

    private static final String PLAYER_ONLY_MESSAGE = "<red>Only players can execute this specific command!";

    protected final ChatColorManager chatColorManager;

    /**
     * Creates the shared command support.
     *
     * @param chatColorManager message manager
     */
    protected CommandSupport(ChatColorManager chatColorManager) {
        this.chatColorManager = chatColorManager;
    }

    /**
     * Requires a command sender to be a player.
     *
     * @param sender command sender
     * @return player sender, or {@code null} when the sender is not a player
     */
    protected Player requirePlayer(CommandSender sender) {
        if (sender instanceof Player player) {
            return player;
        }

        sendMessage(sender, PLAYER_ONLY_MESSAGE);
        return null;
    }

    /**
     * Sends a configured or raw MiniMessage string
     *
     * @param sender message recipient
     * @param message raw message
     */
    protected void sendMessage(CommandSender sender, String message) {
        MessageUtils.send(sender, chatColorManager.getPrefix(), message);
    }

    /**
     * Sends the configured no-permission message.
     *
     * @param sender message recipient
     */
    protected void sendNoPermission(CommandSender sender) {
        sendMessage(sender, chatColorManager.getNoPermsMsg());
    }

    /**
     * Sends the configured incorrect usage message.
     *
     * @param sender message recipient
     */
    protected void sendIncorrectUsage(CommandSender sender) {
        sendMessage(sender, chatColorManager.getIncorrectCmdUsageMsg());
    }
}
